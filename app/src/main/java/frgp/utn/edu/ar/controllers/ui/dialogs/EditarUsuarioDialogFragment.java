package frgp.utn.edu.ar.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Usuario;

public class EditarUsuarioDialogFragment extends DialogFragment {
    TextView titulo;
    EditText pass1, pass2, correo, telefono;
    Usuario selectedUser = null;
    Spinner spinTipo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            selectedUser = (Usuario) args.getSerializable("selected_user");
        }
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_modificar_usuario, null);

        titulo = dialogView.findViewById(R.id.txt_modificar_usuario_titulo);
        spinTipo = dialogView.findViewById(R.id.sp_modificar_usuario_tipo);
        pass1 = dialogView.findViewById(R.id.etx_modificar_usuario_pass1);
        pass2 = dialogView.findViewById(R.id.etx_modificar_usuario_pass2);
        correo = dialogView.findViewById(R.id.etx_modificar_usuario_correo);
        telefono = dialogView.findViewById(R.id.etx_modificar_usuario_telefono);

        // CARGA SPINNER TIPO DE USUARIO
        String[] opciones = {"VECINO", "MODERADOR", "ADMINISTRADOR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, opciones);
        spinTipo.setAdapter(adapter);

        if(selectedUser==null){
            Toast.makeText(getContext(), "ERROR AL CARGAR EL USUARIO", Toast.LENGTH_LONG).show();
            dismiss();
        }

        titulo.setText("Editar perfil de " + selectedUser.getUsername());
        correo.setText(selectedUser.getCorreo());
        telefono.setText(selectedUser.getTelefono());


        /// BOTONES
        Button confirmar = dialogView.findViewById(R.id.btnConfirmarModificarUsuario);
        botonConfirmar(confirmar);

        Button cancelar = dialogView.findViewById(R.id.btnCancelarModificarUsuario);
        botonCancelar(cancelar);

        builder.setView(dialogView);
        return builder.create();
    }

    private void botonConfirmar(Button confirmar){

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void botonCancelar(Button cancelar){
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
