package frgp.utn.edu.ar.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoDenuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoProyecto;
import frgp.utn.edu.ar.controllers.data.model.EstadoReporte;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMAActualizarEstadoDenunciaProyecto;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMAActualizarEstadoDenunciaReporte;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAActualizarEstadoProyecto;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMAActualizarEstadoReporte;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.MailService;
import frgp.utn.edu.ar.controllers.utils.NotificacionService;


public class EliminarPublicacionDialogFragment extends DialogFragment {

    private String motivo;
    private Denuncia seleccionado = null;
    private LogService logService = new LogService();
    private Usuario loggedInUser = null;
    private NotificacionService serviceNotificacion= new NotificacionService();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /// RECUPERA LOS DATOS ENVIADOS DEL BUNDLE
        Bundle args = getArguments();
        if (args != null) {
            /// SI HAY DATOS LOS GUARDA EN VARIABLES
            seleccionado = (Denuncia) args.getSerializable("selected_publicacionDencia");
            loggedInUser = (Usuario) args.getSerializable("logged_in_user");
            motivo = args.getString("motivo_eliminado");
            if(seleccionado == null){
                /// SI NO HAY DATOS CIERRA EL DIALOG CON MENSAJE DE ERROR
                Toast.makeText(getContext(), "ERROR AL CARGAR LA PUBLICACION", Toast.LENGTH_LONG).show();
                dismiss();
            }
        }else{
            Toast.makeText(getContext(), "ERROR AL CARGAR LA PUBLICACION", Toast.LENGTH_LONG).show();
            dismiss();
        }
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_eliminar_publicacion, null);

        /// ASIGNA LOS CONTROLES
        Button btnConfirmar = dialogView.findViewById(R.id.btnDialogConfirmarEliminarPublicacion);
        Button btnCancelar = dialogView.findViewById(R.id.btnDialogCancelarEliminarPublicacion);

        /// ASIGNA Y ESCRIBE EL TITULO
        TextView titulo = dialogView.findViewById(R.id.dialog_titulo_elimiar_publicacion);
        titulo.setText("¿Desea Eliminar la Publicacion N° " + seleccionado.getPublicacion().getId()+ "?");

        /// COMPORTAMIENTO DEL BOTON CONFIRMAR
        comportamiento_boton_confirmar(btnConfirmar);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancelar la valoración y cerrar el diálogo.
                dismiss();
            }
        });
        builder.setView(dialogView);
        return builder.create();
    }
    private void comportamiento_boton_confirmar(Button btnConfirmar){
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean exito = false;
                /// SI LA DENUNCIA ES SOBRE UN REPORTE
                if(seleccionado.getTipo().getTipo().equals("REPORTE")){
                    exito = eliminar_reporte();
                }

                /// SI LA DENUNCIA ES SOBRE UN PROYECTO
                if(seleccionado.getTipo().getTipo().equals("PROYECTO")){
                    exito = eliminar_proyecto();
                }

                /// SI LA DENUNCIA SE PROCESA CON ÉXITO, SE GENERA EL LOG Y SE CIERRA EL DIALOG
                if(exito){
                    logService.log(loggedInUser.getId(), LogsEnum.ELIMINAR_PUBLICACION, String.format("Eliminaste la publicacion %s", seleccionado.getTitulo()));
                    serviceNotificacion.notificacion(seleccionado.getDenunciante().getId(), String.format("Se notifica que la publicacion %s que denunciaste ha sido eliminada por los siguientes motivos: %s", seleccionado.getPublicacion().getTitulo() ,  motivo));
                    serviceNotificacion.notificacion(seleccionado.getPublicacion().getOwner().getId(), String.format("Se notifica la eliminacion de la publicacion %s por los motivos: %s", seleccionado.getPublicacion().getTitulo() ,  motivo));

                    dismiss();
                }else{
                    Toast.makeText(getContext(), "IMPOSIBLE COMPLETAR LA OPERACIÓN", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean eliminar_reporte(){
        /// CREA UNA VARIABLE AUXILIAR Y LE ASIGNA LOS VALORES DE LA PUBLICACION EN LA DENUNCIA
        Reporte eliminar = cargar_reporte();

        /// CAMBIA EL ESTADO DE LA DENUNCIA Y EL REPORTE, GUARDA EL ESTADO ORIGINAL PARA REVERTIR EL PROCESO EN CASO DE FALLA
        eliminar.setEstado(new EstadoReporte(7,"ELIMINADO"));
        seleccionado.setEstado(new EstadoDenuncia(2,"ATENDIDA"));
        EstadoDenuncia estado_orig = seleccionado.getEstado();
        try {
            /// ACTUALIZA EL ESTADO DE LA DENUNCIA
            DMAActualizarEstadoDenunciaReporte DMAActualizarEstadoDenuncia = new DMAActualizarEstadoDenunciaReporte(seleccionado);
            DMAActualizarEstadoDenuncia.execute();
            if(DMAActualizarEstadoDenuncia.get()){
                /// SI SE ACTUALIZA LA DENUNCIA, SE ACTUALIZA EL ESTADO DEL REPORTE
                DMAActualizarEstadoReporte DMAActualizarReporte = new DMAActualizarEstadoReporte(eliminar);
                DMAActualizarReporte.execute();
                if(DMAActualizarReporte.get()){
                    Toast.makeText(getContext(), "REPORTE CERRADO - DENUNCIA ATENDIDA", Toast.LENGTH_LONG).show();
                    return true;
                }else{
                    /// SI FALLA LA ACTUALIZACIÓN DEL REPORTE, SE REVIERTE EL CAMBIO EN LA DENUNCIA
                    seleccionado.setEstado(estado_orig);
                    DMAActualizarEstadoDenuncia = new DMAActualizarEstadoDenunciaReporte(seleccionado);
                    DMAActualizarEstadoDenuncia.execute();
                    Toast.makeText(getContext(), "ERROR - NO SE PUDO CERRAR EL REPORTE", Toast.LENGTH_LONG).show();
                    return false;
                }
            }else{
                Toast.makeText(getContext(), "ERROR - NO SE PUDO PROCESAR EL PEDIDO", Toast.LENGTH_LONG).show();
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    private boolean eliminar_proyecto(){
        /// CREA UNA VARIABLE AUXILIAR Y LE ASIGNA LOS VALORES DE LA PUBLICACION EN LA DENUNCIA
        Proyecto eliminar = cargar_proyecto();

        /// CAMBIA EL ESTADO DE LA DENUNCIA Y EL PROYECTO, GUARDA EL ESTADO ORIGINAL PARA REVERTIR EL PROCESO EN CASO DE FALLA
        eliminar.setEstado(new EstadoProyecto(6,"ELIMINADO"));
        seleccionado.setEstado(new EstadoDenuncia(2,"ATENDIDA"));
        EstadoDenuncia estado_orig = seleccionado.getEstado();
        try {
            /// ACTUALIZA EL ESTADO DE LA DENUNCIA
            DMAActualizarEstadoDenunciaProyecto DMAActualizarEstadoDenuncia = new DMAActualizarEstadoDenunciaProyecto(seleccionado);
            DMAActualizarEstadoDenuncia.execute();
            if(DMAActualizarEstadoDenuncia.get()){
                /// SI SE ACTUALIZA LA DENUNCIA, SE ACTUALIZA EL ESTADO DEL PROYECTO
                DMAActualizarEstadoProyecto DMAActualizarReporte = new DMAActualizarEstadoProyecto(eliminar);
                DMAActualizarReporte.execute();
                if(DMAActualizarReporte.get()){
                    Toast.makeText(getContext(), "PROYECTO CERRADO - DENUNCIA ATENDIDA", Toast.LENGTH_LONG).show();
                    return true;
                }else{
                    /// SI FALLA LA ACTUALIZACIÓN DEL PROYECTO, SE REVIERTE EL CAMBIO EN LA DENUNCIA
                    seleccionado.setEstado(estado_orig);
                    DMAActualizarEstadoDenuncia = new DMAActualizarEstadoDenunciaProyecto(seleccionado);
                    DMAActualizarEstadoDenuncia.execute();
                    Toast.makeText(getContext(), "ERROR - NO SE PUDO CERRAR EL PROYECTO", Toast.LENGTH_LONG).show();
                    return false;
                }
            }else{
                Toast.makeText(getContext(), "ERROR - NO SE PUDO PROCESAR EL PEDIDO", Toast.LENGTH_LONG).show();
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    private Proyecto cargar_proyecto(){
        /// DEVUELVE UN REPORTE CARGADO CON LOS DATOS DE LA PUBLICACION
        Proyecto eliminar = new Proyecto();
        eliminar.setId(seleccionado.getPublicacion().getId());
        eliminar.setTitulo(seleccionado.getPublicacion().getTitulo());
        eliminar.setDescripcion(seleccionado.getPublicacion().getDescripcion());
        eliminar.setOwner(seleccionado.getPublicacion().getOwner());
        eliminar.setFecha(seleccionado.getPublicacion().getFecha());
        eliminar.setLatitud(seleccionado.getPublicacion().getLatitud());
        eliminar.setLongitud(seleccionado.getPublicacion().getLongitud());
        return eliminar;
    }
    private Reporte cargar_reporte(){
        /// DEVUELVE UN PROYECTO CARGADO CON LOS DATOS DE LA PUBLICACION
        Reporte eliminar = new Reporte();
        eliminar.setId(seleccionado.getPublicacion().getId());
        eliminar.setTitulo(seleccionado.getPublicacion().getTitulo());
        eliminar.setDescripcion(seleccionado.getPublicacion().getDescripcion());
        eliminar.setOwner(seleccionado.getPublicacion().getOwner());
        eliminar.setFecha(seleccionado.getPublicacion().getFecha());
        eliminar.setLatitud(seleccionado.getPublicacion().getLatitud());
        eliminar.setLongitud(seleccionado.getPublicacion().getLongitud());
        return eliminar;
    }
}
