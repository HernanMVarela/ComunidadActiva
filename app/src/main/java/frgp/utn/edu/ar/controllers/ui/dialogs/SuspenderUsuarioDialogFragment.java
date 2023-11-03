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
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMACambiarEstadoUsuario;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.MailService;
import frgp.utn.edu.ar.controllers.utils.NotificacionService;

public class SuspenderUsuarioDialogFragment extends DialogFragment {

    Button btnConfirmar;
    Button btnCancelar;
    String motivo;
    Usuario selectedUser = null;
    LogService logService = new LogService();
    MailService mailService = new MailService();
    private Usuario loggedInUser = null;
    NotificacionService serviceNotificacion= new NotificacionService();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            selectedUser = (Usuario) args.getSerializable("selected_userPublicacion");
            loggedInUser = (Usuario) args.getSerializable("logged_in_user");
            motivo = args.getString("mi_string");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_suspender_usuario, null);

        btnConfirmar = dialogView.findViewById(R.id.btnDialogConfirmarSuspenderUsuario);
        btnCancelar = dialogView.findViewById(R.id.btnDialogCancelarSuspenderUsuario);

        TextView titulo = dialogView.findViewById(R.id.dialog_titulo_suspender_usuario);

        if(selectedUser==null){
            Toast.makeText(getContext(), "ERROR AL CARGAR EL USUARIO", Toast.LENGTH_LONG).show();
            dismiss();
        }
        titulo.setText("¿Desea Suspender al Usuario " + selectedUser.getUsername() + "?");
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedUser.getEstado().getEstado().equals("ACTIVO")){
                    /// SI ESTA ACTIVO, SE SUSPENDE AL USUARIO
                    selectedUser.setEstado(new EstadoUsuario(3,"SUSPENDIDO"));
                    logService.log(loggedInUser.getId(), LogsEnum.SUSPENSION_USUARIO, String.format("Suspendiste al usuario %s", selectedUser.getUsername()));
                    mailService.sendMail(selectedUser.getCorreo(), "COMUNIDAD ACTIVA - SUSPENSION DE USUARIO", "Su usuario ha sido suspendido por un MODERADOR.");
                    serviceNotificacion.notificacion(selectedUser.getId(),"Se notifica la suspencion: " + selectedUser.getId() +" por los motivos: "+ motivo);

                    DMACambiarEstadoUsuario DMACambiarEstadoUser = new DMACambiarEstadoUsuario(selectedUser,getContext());
                    DMACambiarEstadoUser.execute();
                } else {
                    Toast.makeText(getContext(), "No se pudo realializar la accion, usuario: "+ selectedUser.getEstado().getEstado(), Toast.LENGTH_LONG).show();
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
