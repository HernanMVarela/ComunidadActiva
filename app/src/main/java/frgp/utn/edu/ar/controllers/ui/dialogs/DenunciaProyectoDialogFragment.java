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

import java.util.Date;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoDenuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoProyecto;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.model.TipoDenuncia;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMAGuardarDenunciaProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAActualizarEstadoProyecto;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.NotificacionService;

public class DenunciaProyectoDialogFragment extends DialogFragment {
    private EditText titulo, descripcion;
    private Proyecto selectedProyect = null;
    private Usuario loggedInUser = null;
    LogService logService = new LogService();
    NotificacionService notificacionService = new NotificacionService();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            selectedProyect = (Proyecto) args.getSerializable("selected_proyect");
            loggedInUser = (Usuario) args.getSerializable("logged_in_user");
        }
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_denunciar_proyecto, null);

        Button btnAceptar = dialogView.findViewById(R.id.btnConfirmarDenunciaP);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelarDenunciaP);
        titulo = dialogView.findViewById(R.id.edTituloDenunciaP);
        descripcion = dialogView.findViewById(R.id.edDescDenunciaP);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){

                    /// CARGA DATOS EN LA DENUNCIA
                    Denuncia nueva = new Denuncia();
                    nueva.setPublicacion(selectedProyect);
                    nueva.setTipo(new TipoDenuncia(2, "PROYECTO"));
                    nueva.setDenunciante(loggedInUser);
                    nueva.setDescripcion(descripcion.getText().toString());
                    nueva.setTitulo(titulo.getText().toString());
                    nueva.setEstado(new EstadoDenuncia(1, "PENDIENTE"));
                    nueva.setFecha_creacion(new Date(System.currentTimeMillis()));

                    try {
                        /// ITENTA GUARDAR LA DENUNCIA Y CAMBIAR EL ESTADO DEL REPORTE
                        DMAGuardarDenunciaProyecto DMAGuardarDen = new DMAGuardarDenunciaProyecto(nueva);
                        DMAGuardarDen.execute();
                        if (DMAGuardarDen.get()) {
                            Toast.makeText(getContext(), "Denuncia guardada", Toast.LENGTH_SHORT).show();
                            Proyecto modificar = (Proyecto) nueva.getPublicacion();
                            modificar.setEstado(new EstadoProyecto(5, "DENUNCIADO"));
                            DMAActualizarEstadoProyecto DMAEstadoProyecto = new DMAActualizarEstadoProyecto(modificar);
                            DMAEstadoProyecto.execute();
                            if (DMAEstadoProyecto.get()) {
                                logService.log(loggedInUser.getId(), LogsEnum.DENUNCIA_REPORTE, String.format("Denunciaste el reporte %s", selectedProyect.getTitulo()));
                                notificacionService.notificacion(selectedProyect.getOwner().getId(), String.format("El %s %s denuncio el reporte %s", loggedInUser.getTipo().getTipo(), loggedInUser.getUsername(), selectedProyect.getTitulo()));
                            }else{
                                Toast.makeText(getContext(), "Error al cambiar el estado", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "No se pudo crear la denuncia", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dismiss();
            }
        });


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(dialogView);
        return builder.create();
    }

    public boolean validarCampos(){
        if(titulo.getText().toString().trim().isEmpty() || titulo.getText().toString().trim().length()<5){
            Toast.makeText(getContext(), "Debes ingresar un título de al menos 4 carácteres", Toast.LENGTH_LONG).show();
            return false;
        }
        if(descripcion.getText().toString().trim().isEmpty() || descripcion.getText().toString().trim().length()<5){
            Toast.makeText(getContext(), "Debes ingresar una descripción de al menos 4 carácteres", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
