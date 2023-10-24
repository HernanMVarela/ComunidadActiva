package frgp.utn.edu.ar.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoDenuncia;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.TipoDenuncia;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.denuncias.DMAGuardarDenunciaReporte;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMACargarCierreReporte;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMACerrarReporte;

public class CerrarReporteDialogFragment extends DialogFragment {
    private TextView titulo, atendido, descripcion;
    private ImageView imagen;
    private Reporte selectedReport = null;
    private Usuario loggedInUser = null;
    private CierreReporte cierreReporte = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            selectedReport = (Reporte) args.getSerializable("selected_report");
            loggedInUser = (Usuario) args.getSerializable("logged_in_user");
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
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        btnCerrarRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cierreReporte!=null){
                    DMACerrarReporte dmaCerrarRep = new DMACerrarReporte(cierreReporte,getContext());
                    dmaCerrarRep.execute();
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
                // Cierra el diálogo.
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

    private void modificarEstadoReporte(Reporte reporte){

    }
}
