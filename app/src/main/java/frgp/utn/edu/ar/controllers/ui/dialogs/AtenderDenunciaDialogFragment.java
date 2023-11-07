package frgp.utn.edu.ar.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import frgp.utn.edu.ar.controllers.data.remote.denuncias.DMAGuardarDenunciaProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAActualizarEstadoProyecto;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMAActualizarEstadoReporte;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.NotificacionService;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class AtenderDenunciaDialogFragment extends DialogFragment {
    private EditText resolucion;
    private Denuncia seleccionado;
    private Usuario loggedInUser = null;
    private SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private NotificacionService serviceNotificacion= new NotificacionService();
    private LogService logService = new LogService();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        loggedInUser = sharedPreferences.getUsuarioData(getContext());
        Bundle bundle = this.getArguments();
        /// OBTIENE LA DENUNCIA SELECCIONADA EN LA PANTALLA ANTERIOR
        if (bundle != null) {
            seleccionado = (Denuncia) bundle.getSerializable("selected_denuncia");
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
            dismiss();
        }
        if(seleccionado == null) {
            Toast.makeText(getContext(), "Error al cargar la denuncia!", Toast.LENGTH_LONG).show();
            dismiss();
        }
        if(seleccionado.getEstado().getEstado().equals("PENDIENTE")) {
            seleccionado.setEstado(new EstadoDenuncia(2, "CERRADA"));
            boolean exito = false;
            Log.i("LOG_DENUNCIA", "Tipo: "+seleccionado.getTipo());
            if(seleccionado.getTipo().getTipo().equals("REPORTE")){
                exito = actualizar_reporte();
            }else if (seleccionado.getTipo().getTipo().equals("PROYECTO")){
                exito = actualizar_proyecto();
            }
            if(exito){
                logService.log(loggedInUser.getId(), LogsEnum.CERRAR_DENUNCIA_Y_NOTIFICAR, String.format("DESESTIMASTE la Denuncia %s", seleccionado.getTitulo()));
                serviceNotificacion.notificacion(seleccionado.getDenunciante().getId(), "Se notifica la cancelación de la Denuncia sobre la publicacion: " + seleccionado.getPublicacion().getId() + " por los motivos: " + resolucion.getText().toString());
            }else{
                Toast.makeText(getContext(), "No se pudo completar el proceso.", Toast.LENGTH_LONG).show();
            }
        }
        dismiss();
    }
    private void comportamiento_boton_cerrar(){
        if(!validarCampos()){
            dismiss();
        }
        if(seleccionado == null) {
            Toast.makeText(getContext(), "Error al cargar la denuncia!", Toast.LENGTH_LONG).show();
            dismiss();
        }
        if(seleccionado.getEstado().getEstado().equals("PENDIENTE")) {
            seleccionado.setEstado(new EstadoDenuncia(2, "ATENDIDA"));
            boolean exito = false;
            if(seleccionado.getPublicacion().getClass().isInstance(Reporte.class)){
                exito = cerrar_reporte();
            }else if (seleccionado.getPublicacion().getClass().isInstance(Proyecto.class)){
                exito = cerrar_proyecto();
            }
            if(exito){
                logService.log(loggedInUser.getId(), LogsEnum.CERRAR_DENUNCIA_Y_NOTIFICAR, String.format("CERRASTE la Denuncia %s", seleccionado.getTitulo()));
                serviceNotificacion.notificacion(seleccionado.getDenunciante().getId(), "Se notifica el cierre de la Denuncia sobre la publicacion: " + seleccionado.getPublicacion().getId() + " por los motivos: " + resolucion);
            }else{
                Toast.makeText(getContext(), "No se pudo completar el proceso.", Toast.LENGTH_LONG).show();
            }
        }
        dismiss();
    }
    private boolean cerrar_reporte(){
        try{
            DMAActualizarEstadoDenunciaProyecto DMAEstadoDenuncia = new DMAActualizarEstadoDenunciaProyecto(seleccionado);
            DMAEstadoDenuncia.execute();
            if(DMAEstadoDenuncia.get()){
                Proyecto modificar = (Proyecto) seleccionado.getPublicacion();
                modificar.setEstado(new EstadoProyecto(4,"CANCELADO"));
                return actualizar_estado_proyecto(modificar);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean cerrar_proyecto(){
        try{
            DMAActualizarEstadoDenunciaReporte DMAEstadoDenuncia = new DMAActualizarEstadoDenunciaReporte(seleccionado);
            DMAEstadoDenuncia.execute();
            if(DMAEstadoDenuncia.get()){
                Reporte modificar = (Reporte) seleccionado.getPublicacion();
                modificar.setEstado(new EstadoReporte(4,"CERRADO"));
                return actualizar_estado_reporte(modificar);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean actualizar_reporte(){
        try{
            Log.i("LOG_DENUNCIA", "ACCESO A ACTUALIZAR DENUNCIA");
            DMAActualizarEstadoDenunciaReporte DMAEstadoDenuncia = new DMAActualizarEstadoDenunciaReporte(seleccionado);
            DMAEstadoDenuncia.execute();
            if(DMAEstadoDenuncia.get()){
                Reporte modificar = cargar_datos_reporte();
                modificar.setEstado(new EstadoReporte(1,"ABIERTO"));
                return actualizar_estado_reporte(modificar);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean actualizar_estado_reporte(Reporte modificar){
        try {
            Log.i("LOG_DENUNCIA", "ACCESO A ACTUALIZAR REPORTE");
            DMAActualizarEstadoReporte DMAActualizarReporte = new DMAActualizarEstadoReporte(modificar);
            DMAActualizarReporte.execute();
            return DMAActualizarReporte.get();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean actualizar_proyecto(){
        try{
            DMAActualizarEstadoDenunciaProyecto DMAEstadoDenuncia = new DMAActualizarEstadoDenunciaProyecto(seleccionado);
            DMAEstadoDenuncia.execute();
            if(DMAEstadoDenuncia.get()){
                Proyecto modificar = cargar_datos_proyecto();
                modificar.setEstado(new EstadoProyecto(1,"ABIERTO"));
                return actualizar_estado_proyecto(modificar);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean actualizar_estado_proyecto(Proyecto modificar){
        try {
            DMAActualizarEstadoProyecto DMAActualizarProyecto = new DMAActualizarEstadoProyecto(modificar);
            DMAActualizarProyecto.execute();
            return DMAActualizarProyecto.get();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private Reporte cargar_datos_reporte(){
        Reporte modificar = new Reporte();
        modificar.setId(seleccionado.getPublicacion().getId());
        modificar.setTitulo(seleccionado.getPublicacion().getTitulo());
        modificar.setOwner(seleccionado.getPublicacion().getOwner());
        modificar.setLatitud(seleccionado.getPublicacion().getLatitud());
        modificar.setLongitud(seleccionado.getPublicacion().getLongitud());
        return modificar;
    }

    private Proyecto cargar_datos_proyecto(){
        Proyecto modificar = new Proyecto();
        modificar.setId(seleccionado.getPublicacion().getId());
        modificar.setTitulo(seleccionado.getPublicacion().getTitulo());
        modificar.setOwner(seleccionado.getPublicacion().getOwner());
        modificar.setLatitud(seleccionado.getPublicacion().getLatitud());
        modificar.setLongitud(seleccionado.getPublicacion().getLongitud());
        return modificar;
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
