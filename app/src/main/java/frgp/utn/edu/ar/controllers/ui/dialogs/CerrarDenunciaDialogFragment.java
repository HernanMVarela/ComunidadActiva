package frgp.utn.edu.ar.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoDenuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoProyecto;
import frgp.utn.edu.ar.controllers.data.model.EstadoReporte;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.repository.denuncia.DenunciaRepository;
import frgp.utn.edu.ar.controllers.data.repository.proyecto.ProyectoRepository;
import frgp.utn.edu.ar.controllers.data.repository.reporte.ReporteRepository;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.MailService;
import frgp.utn.edu.ar.controllers.utils.NotificacionService;

public class CerrarDenunciaDialogFragment extends DialogFragment {
    private EditText resolucion;
    private Denuncia seleccionado = null;
    private Usuario loggedInUser = null;
    private NotificacionService serviceNotificacion= new NotificacionService();
    private LogService logService = new LogService();
    private MailService mailService = new MailService();
    private DenunciaRepository denunciaRepository = new DenunciaRepository();
    private ReporteRepository reporteRepository = new ReporteRepository();
    private ProyectoRepository proyectoRepository = new ProyectoRepository();
    private Reporte reporte;
    private Proyecto proyecto;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            seleccionado = (Denuncia) args.getSerializable("selected_denuncia");
            loggedInUser = (Usuario) args.getSerializable("logged_user");
        }else{
            Toast.makeText(getContext(), "No se pudo cargar la denuncia!", Toast.LENGTH_LONG).show();
            dismiss();
        }
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_atender_denuncia, null);

        Button bDesestimar = dialogView.findViewById(R.id.btnDesestimarDenuncia);
        Button bCerrarDenuncia = dialogView.findViewById(R.id.btnCerrarDenuncia);
        resolucion = dialogView.findViewById(R.id.etResolucionDenuncia);

        if(seleccionado.getTipo().getTipo().equals("REPORTE")) {
            reporte =  reporteRepository.buscarReportePorId(seleccionado.getPublicacion().getId());
            if(reporte.getEstado().getEstado().equals("ELIMINADO")){
                bDesestimar.setVisibility(View.GONE);
            }
        }

        if(seleccionado.getTipo().getTipo().equals("PROYECTO")){
            proyecto = proyectoRepository.buscarProyectoPorId(seleccionado.getPublicacion().getId());
            if(proyecto.getEstado().getEstado().equals("ELIMINADO")){
                bDesestimar.setVisibility(View.GONE);
            }
        }

        if(seleccionado.getEstado().getEstado().equals("ATENDIDA")){
            bDesestimar.setVisibility(View.GONE);
        }

        bDesestimar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comportamiento_boton_desestimar();
            }
        });

        bCerrarDenuncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comportamiento_boton_cerrar();
            }
        });
        builder.setView(dialogView);
        return builder.create();
    }
    private void comportamiento_boton_desestimar(){
        if(!validarCampos()) {
            return;
        }

        if(seleccionado == null) {
            Toast.makeText(getContext(), "Error al cargar la denuncia!", Toast.LENGTH_LONG).show();
            dismiss();
        }

        if(desestimarDenuncia()){
            logService.log(loggedInUser.getId(), LogsEnum.DESESTIMAR_DENUNCIA, String.format("Desestimaste la Denuncia %s", seleccionado.getTitulo()));
            serviceNotificacion.notificacion(seleccionado.getDenunciante().getId(), String.format("Se notifica la cancelacion de la Denuncia sobre la publicacion %s por los motivos $s.",seleccionado.getPublicacion().getTitulo(), resolucion.getText().toString()));
            serviceNotificacion.notificacion(seleccionado.getPublicacion().getOwner().getId(), String.format("Se notifica la cancelacion de la Denuncia sobre la publicacion %s por los motivos $s.",seleccionado.getPublicacion().getTitulo(), resolucion.getText().toString()));
            mailService.sendMail(seleccionado.getDenunciante().getCorreo(), "Denuncia desestimada", String.format("Se desestimo la denuncia sobre la publicacion %s por los motivos %s.",seleccionado.getPublicacion().getTitulo(), resolucion.getText().toString()));
            mailService.sendMail(seleccionado.getPublicacion().getOwner().getCorreo(), "Denuncia desestimada", String.format("Se desestimo la denuncia sobre la publicacion %s por los motivos %s.",seleccionado.getPublicacion().getTitulo(), resolucion.getText().toString()));
            Toast.makeText(getContext(), "Denuncia desestimada correctamente.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), "No se pudo completar el proceso.", Toast.LENGTH_LONG).show();
        }
        dismiss();
    }
    private void comportamiento_boton_cerrar(){
        if(!validarCampos()){
            return;
        }
        if(seleccionado == null) {
            Toast.makeText(getContext(), "Error al cargar la denuncia!", Toast.LENGTH_LONG).show();
            dismiss();
        }
        if(seleccionado.getEstado().getEstado().equals("ATENDIDA")){
            if(cerrar_denuncia()){
                logService.log(loggedInUser.getId(), LogsEnum.CERRAR_DENUNCIA, String.format("CERRASTE la Denuncia %s", seleccionado.getTitulo()));
                serviceNotificacion.notificacion(seleccionado.getDenunciante().getId(), String.format("Se notifica el cierre de la Denuncia sobre la publicacion %s por los motivos $s.",seleccionado.getPublicacion().getTitulo(), resolucion.getText()));
                serviceNotificacion.notificacion(seleccionado.getPublicacion().getOwner().getId(), String.format("Se notifica el cierre de la Denuncia sobre la publicacion %s por los motivos $s.",seleccionado.getPublicacion().getTitulo(), resolucion.getText().toString()));
                mailService.sendMail(seleccionado.getDenunciante().getCorreo(), "Denuncia cerrada", String.format("Se cerro la denuncia sobre la publicacion %s por los motivos %s.",seleccionado.getPublicacion().getTitulo(), resolucion.getText().toString()));
                mailService.sendMail(seleccionado.getPublicacion().getOwner().getCorreo(), "Denuncia cerrada", String.format("Se cerro la denuncia sobre la publicacion %s por los motivos %s.",seleccionado.getPublicacion().getTitulo(), resolucion.getText().toString()));
                Toast.makeText(getContext(), "Denuncia cerrada correctamente.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getContext(), "No se pudo completar el proceso.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getContext(), "La denuncia debe ser ATENDIDA para poder cerrarla.", Toast.LENGTH_LONG).show();
        }
        dismiss();
    }

    private boolean cerrar_denuncia(){
        seleccionado.setEstado(new EstadoDenuncia(3, "CERRADA"));

        if(seleccionado.getTipo().getTipo().equals("REPORTE")) {
            denunciaRepository.cambiarEstadoDenunciaReporte(seleccionado);
            if(!reporte.getEstado().getEstado().equals("ELIMINADO")){
                reporte.setEstado(new EstadoReporte(4,"CERRADO"));
                reporteRepository.actualizarEstadoReporte(reporte);
            }
            return true;
        }

        if(seleccionado.getTipo().getTipo().equals("PROYECTO")) {
            denunciaRepository.cambiarEstadoDenunciaProyecto(seleccionado);
            if(!proyecto.getEstado().getEstado().equals("ELIMINADO")){
                proyecto.setEstado(new EstadoProyecto(7,"CERRADO"));
                proyectoRepository.actualizarEstadoProyecto(proyecto);
            }
            return true;
        }
        return false;
    }

    private boolean desestimarDenuncia() {
        seleccionado.setEstado(new EstadoDenuncia(4, "CANCELADA"));
        if(seleccionado.getTipo().getTipo().equals("REPORTE")) {
            denunciaRepository.cambiarEstadoDenunciaReporte(seleccionado);
            reporte.setEstado(new EstadoReporte(1,"ABIERTO"));
            reporteRepository.actualizarEstadoReporte(reporte);
            return true;
        }

        if(seleccionado.getTipo().getTipo().equals("PROYECTO")) {
            denunciaRepository.cambiarEstadoDenunciaProyecto(seleccionado);
            proyecto.setEstado(new EstadoProyecto(1,"ABIERTO"));
            proyectoRepository.actualizarEstadoProyecto(proyecto);
            return true;
        }
        return false;
    }

    private boolean validarCampos(){
        if(resolucion.getText().toString().trim().isEmpty() || resolucion.getText().toString().trim().length()<5){
            Toast.makeText(getContext(), "Debes ingresar una resolución de al menos 4 carácteres", Toast.LENGTH_LONG).show();
            return false;
        }
        if(resolucion.getText().toString().trim().length()>201){
            Toast.makeText(getContext(), "La resolución debe tener como máximo 200 caractéres", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
