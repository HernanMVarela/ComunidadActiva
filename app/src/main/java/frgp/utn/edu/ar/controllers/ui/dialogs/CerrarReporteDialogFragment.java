package frgp.utn.edu.ar.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.CierreReporte;
import frgp.utn.edu.ar.controllers.data.model.EstadoReporte;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMAActualizarEstadoReporte;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMACargarCierreReporte;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMACerrarReporte;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMAModificarPuntajeUsuario;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.NotificacionService;

public class CerrarReporteDialogFragment extends DialogFragment {
    private TextView titulo, atendido, descripcion;
    private ImageView imagen;
    private Reporte selectedReport = null;
    private Usuario loggedInUser = null;
    private CierreReporte cierreReporte = null;
    private final LogService logService = new LogService();
    private final NotificacionService notificacionService = new NotificacionService();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            selectedReport = (Reporte) args.getSerializable("selected_report");
            loggedInUser = (Usuario) args.getSerializable("logged_in_user");
        }else{
            Toast.makeText(getContext(), "No se pudo cargar el reporte!", Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_cerrar_reporte, null);

        Button btnCerrarRep = dialogView.findViewById(R.id.btnCerrarReporte);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelarCerrarReporte);
        Button btnRearbrir = dialogView.findViewById(R.id.btnReabrirReporte);
        titulo = dialogView.findViewById(R.id.dialog_cerrar_reporte_titulo);
        descripcion = dialogView.findViewById(R.id.dialog_cerrar_reporte_descripcion);
        atendido = dialogView.findViewById(R.id.dialog_cerrar_reporte_fecha_at);
        imagen = dialogView.findViewById(R.id.dialog_cerrar_reporte_imagen);

        if(selectedReport != null){
            titulo.setText(selectedReport.getTitulo());
            try {
                DMACargarCierreReporte dmaCierreReporte = new DMACargarCierreReporte(selectedReport.getId());
                dmaCierreReporte.execute();
                cierreReporte = dmaCierreReporte.get();
                if(cierreReporte!=null){
                    cargarControles(cierreReporte);
                }else{
                    Toast.makeText(getContext(), "No se pudo cargar el reporte!", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        btnCerrarRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cierreReporte!=null){
                    cierreReporte.setEstado(new EstadoReporte(4,"CERRADO"));
                    cierreReporte.getReporte().setEstado(new EstadoReporte(3,"ATENDIDO"));
                    /// SE CIERRA EL REPORTE
                    if(cerrarCierreReporte()) {
                        try {
                            /// SI SE CIERRA CORRECTAMENTE - SE ACTUALIZA EL ESTADO DEL REPORTE
                            DMAActualizarEstadoReporte dmaActualizar = new DMAActualizarEstadoReporte(cierreReporte.getReporte());
                            dmaActualizar.execute();
                            if(dmaActualizar.get()){
                                logService.log(loggedInUser.getId(), LogsEnum.CIERRE_REPORTE, "Se cerró el reporte " + selectedReport.getId());
                                Toast.makeText(getContext(), "Reporte cerrado!", Toast.LENGTH_SHORT).show();

                                /// PUNTAJE - USUARIO ATENDEDOR
                                int puntaje = cierreReporte.getUser().getPuntuacion() + 3;
                                cierreReporte.getUser().setPuntuacion(puntaje);
                                modificar_puntaje_usuario(cierreReporte.getUser());

                                /// PUNTAJE - CREADOR DEL PROYECTO
                                puntaje = selectedReport.getOwner().getPuntuacion() + 1;
                                selectedReport.getOwner().setPuntuacion(puntaje);
                                modificar_puntaje_usuario(selectedReport.getOwner());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(getContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancelar la denuncia y cerrar el diálogo.
                dismiss();
            }
        });

        btnRearbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cierreReporte.setEstado(new EstadoReporte(5,"CANCELADO"));
                cierreReporte.getReporte().setEstado(new EstadoReporte(1,"ABIERTO"));
                // SE RECHAZA LA SOLICITUD DE CIERRE
                if(cerrarCierreReporte()) {
                    try {
                        /// SI SE REABRE CORRECTAMENTE - SE ACTUALIZA EL ESTADO DEL REPORTE
                        DMAActualizarEstadoReporte dmaActualizar = new DMAActualizarEstadoReporte(cierreReporte.getReporte());
                        dmaActualizar.execute();
                        if(dmaActualizar.get()){
                            logService.log(loggedInUser.getId(), LogsEnum.REAPERTURA_REPORTE, "Se reabrio el reporte " + selectedReport.getId());
                            Toast.makeText(getContext(), "Solicitud rechazada!", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                dismiss();
            }
        });
        builder.setView(dialogView);
        return builder.create();
    }

    private void cargarControles(CierreReporte cierreReporte){
        descripcion.setText(cierreReporte.getMotivo());
        Date fechaCierre = cierreReporte.getFechaCierreAsDate();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = formatoFecha.format(fechaCierre);
        atendido.setText("Atendido por " + cierreReporte.getUser().getUsername() + " el día "+ fechaFormateada);
        imagen.setImageBitmap(cierreReporte.getImagen());
    }

    private boolean cerrarCierreReporte(){
        try {
            DMACerrarReporte dmaCerrarReporte = new DMACerrarReporte(cierreReporte);
            dmaCerrarReporte.execute();
            return dmaCerrarReporte.get();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    private void modificar_puntaje_usuario(Usuario user){
        DMAModificarPuntajeUsuario DMAPuntaje = new DMAModificarPuntajeUsuario(user);
        DMAPuntaje.execute();
    }
}
