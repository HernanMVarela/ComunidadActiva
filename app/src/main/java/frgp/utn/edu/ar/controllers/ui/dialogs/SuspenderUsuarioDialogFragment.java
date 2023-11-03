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
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMACambiarEstadoUsuario;

public class SuspenderUsuarioDialogFragment extends DialogFragment {

    private Button btnConfirmar;
    private Button btnCancelar;
    private Usuario selectedUser = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            selectedUser = (Usuario) args.getSerializable("selected_userPublicacion");
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
                // CAMBIAR ESTADO DEL USUARIO EN DB

                DMACambiarEstadoUsuario DMAEstadoUser = new DMACambiarEstadoUsuario(selectedUser,getContext());
                DMAEstadoUser.execute();
                /// REGISTRAR LOG SI ES NECESARIO

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
