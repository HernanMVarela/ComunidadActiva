package frgp.utn.edu.ar.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.ReseniaReporte;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMAVerificarUsuarioVoto;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMACambiarEstadoUsuario;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.MailService;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class EliminarUsuarioDialogFragment extends DialogFragment {
    private Button btnAceptar;
    private Button btnCancelar;
    private RatingBar rtbValoracion;
    private Usuario selectedUser = null;
    private Usuario loggedInUser = null;
    private LogService logService = new LogService();
    private MailService mailService = new MailService();
    private SharedPreferencesService sharedPreferences = new SharedPreferencesService();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            selectedUser = (Usuario) args.getSerializable("selected_user");
        }
        loggedInUser = sharedPreferences.getUsuarioData(getContext());
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_eliminar_usuario, null);

        btnAceptar = dialogView.findViewById(R.id.btnConfirmarEliminarUsuario);
        btnCancelar = dialogView.findViewById(R.id.btnCancelarEliminarUsuario);

        TextView titulo = dialogView.findViewById(R.id.dialog_titulo_eliminar_usuario);

        if(selectedUser==null){
            Toast.makeText(getContext(), "ERROR AL CARGAR EL USUARIO", Toast.LENGTH_LONG).show();
            dismiss();
        }
        titulo.setText("¿Desea eliminar al usuario " + selectedUser.getUsername() + "?");
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CAMBIAR ESTADO DEL USUARIO EN DB

                DMACambiarEstadoUsuario DMAEstadoUser = new DMACambiarEstadoUsuario(selectedUser,getContext());
                DMAEstadoUser.execute();
                /// REGISTRAR LOG SI ES NECESARIO
                logService.log(loggedInUser.getId(), LogsEnum.ELIMINACION_USUARIO, String.format("Eliminaste al usuario %s", selectedUser.getUsername()));
                mailService.sendMail(selectedUser.getCorreo(), "COMUNIDAD ACTIVA - Eliminación de cuenta", "Su cuenta ha sido eliminada por un administrador.");
                //RETURN TO LISTADO USERS
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
