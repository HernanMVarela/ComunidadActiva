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

import java.util.concurrent.ExecutionException;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoDenuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoProyecto;
import frgp.utn.edu.ar.controllers.data.model.EstadoReporte;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAActualizarEstadoProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMABuscarPoyectoId;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAUpdateProyecto;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMAActualizarEstadoReporte;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMACerrarReporte;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMACambiarEstadoUsuario;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.MailService;
import frgp.utn.edu.ar.controllers.utils.NotificacionService;


public class EliminarPublicacionReporteDialogFragment extends DialogFragment {
    Button btnConfirmar;
    Button btnCancelar;
    String motivo;
    Denuncia selectedDenuncia = null;
    LogService logService = new LogService();
    MailService mailService = new MailService();
    private Usuario loggedInUser = null;
    private Reporte reporte;
    NotificacionService serviceNotificacion= new NotificacionService();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            selectedDenuncia = (Denuncia) args.getSerializable("selected_publicacionDencia");
            loggedInUser = (Usuario) args.getSerializable("logged_in_user");
            motivo = args.getString("mi_string");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_eliminar_publicacion, null);

        btnConfirmar = dialogView.findViewById(R.id.btnDialogConfirmarEliminarPublicacion);
        btnCancelar = dialogView.findViewById(R.id.btnDialogCancelarEliminarPublicacion);

        TextView titulo = dialogView.findViewById(R.id.dialog_titulo_elimiar_publicacion);

        if(selectedDenuncia==null){
            Toast.makeText(getContext(), "ERROR AL CARGAR LA PUBLICACION", Toast.LENGTH_LONG).show();
            dismiss();
        }
        titulo.setText("¿Desea Eliminar la Publicacion N° " + selectedDenuncia.getPublicacion().getId()+ "?");
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getContext(), selectedDenuncia.getEstado().getEstado(), Toast.LENGTH_SHORT).show();
                if(selectedDenuncia.getEstado().getEstado().equals("PENDIENTE")){
                    //Toast.makeText(getContext(), selectedDenuncia.getTipo().getTipo(), Toast.LENGTH_SHORT).show();

                    // ACA SE EJECUTA EL DMA
                    // Ejecutar el DMA para obtener el proyecto
                    // int idProyecto = selectedDenuncia.getPublicacion().getId();
                    // DMABuscarPoyectoId tareaObtenerProyecto = new DMABuscarPoyectoId(getContext());
                    // tareaObtenerProyecto.execute(idProyecto);
                   // Proyecto proyecto = tareaObtenerProyecto.get();




                    if(selectedDenuncia.getTipo().getTipo().equals("REPORTE")){
                        Reporte reporte = new Reporte();
                        reporte.setId(selectedDenuncia.getPublicacion().getId());

                        reporte.setEstado(new EstadoReporte(5,"CANCELADO"));
                       // Toast.makeText(getContext(),  reporte.getEstado().getEstado(), Toast.LENGTH_LONG).show();
                        DMAActualizarEstadoReporte DMAEliminarPublicacion = new DMAActualizarEstadoReporte(reporte,getContext());
                        DMAEliminarPublicacion.execute();
                        serviceNotificacion.notificacion(selectedDenuncia.getPublicacion().getOwner().getId(),"Se notifica Su Publicacion: " + selectedDenuncia.getPublicacion().getId() +"ha sido Eliminada, por los motivos: "+ motivo);
                    }
                    if(selectedDenuncia.getTipo().getTipo().equals("PROYECTO")){
                        Proyecto proyecto = new Proyecto();
                        proyecto.setId(selectedDenuncia.getPublicacion().getId());

                        proyecto.setEstado(new EstadoProyecto(5,"CANCELADO"));
                       // Toast.makeText(getContext(),  reporte.getEstado().getEstado(), Toast.LENGTH_LONG).show();
                        DMAActualizarEstadoProyecto DMAEliminarPublicacion = new DMAActualizarEstadoProyecto(getContext(),proyecto);
                        DMAEliminarPublicacion.execute();
                        serviceNotificacion.notificacion(selectedDenuncia.getPublicacion().getOwner().getId(),"Se notifica Su Publicacion: " + selectedDenuncia.getPublicacion().getId() +"ha sido Eliminada, por los motivos: "+ motivo);
                    }
                    /*  if(selectedDenuncia.getTipo().getTipo().equals("REPORTE")){
                        Reporte reporte = new Reporte();
                        reporte.setId(selectedDenuncia.getPublicacion().getId());

                        reporte.setEstado(new EstadoReporte(5,"CANCELADO"));

                        logService.log(loggedInUser.getId(), LogsEnum.ELIMINAR_PUBLICACION, String.format("Suspendiste al usuario %s", reporte.getOwner()));
                        mailService.sendMail(reporte.getOwner().getCorreo(), "COMUNIDAD ACTIVA - ELIMINO LA PUBLICACION", "Su Publicacion ha sido Eliminada por un MODERADOR.");
                        serviceNotificacion.notificacion(selectedDenuncia.getPublicacion().getOwner().getId(),"Se notifica Su Publicacion: " + selectedDenuncia.getPublicacion().getId() +"ha sido Eliminada, por los motivos: "+ motivo);

                        DMAActualizarEstadoReporte DMAEliminarPublicacion = new DMAActualizarEstadoReporte(reporte,getContext());
                        DMAEliminarPublicacion.execute();
                    }*/
                   /*  if(selectedDenuncia.getTipo().getTipo().equals("PROYECTO")){
                        Proyecto proyecto = (Proyecto) selectedDenuncia.getPublicacion();
                        proyecto.setEstado(new EstadoProyecto(5,"CANCELADO"));

                        logService.log(loggedInUser.getId(), LogsEnum.ELIMINAR_PUBLICACION, String.format("Suspendiste al usuario %s", reporte.getOwner()));
                        mailService.sendMail(reporte.getOwner().getCorreo(), "COMUNIDAD ACTIVA - ELIMINO LA PUBLICACION", "Su Publicacion ha sido Eliminada por un MODERADOR.");
                        serviceNotificacion.notificacion(selectedDenuncia.getPublicacion().getOwner().getId(),"Se notifica Su Publicacion: " + selectedDenuncia.getPublicacion().getId() +"ha sido Eliminada, por los motivos: "+ motivo);

                        DMAUpdateProyecto DMAEliminarPublicacion = new DMAUpdateProyecto(reporte.getId(),reporte.getEstado().getId(), getContext());
                        DMAEliminarPublicacion.execute();
                    }*/


                } else {
                    Toast.makeText(getContext(), "No se pudo realializar la accion, la publicación esta: "+ selectedDenuncia.getEstado().getEstado(), Toast.LENGTH_LONG).show();
                }


                dismiss();
            }
        });

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

}
