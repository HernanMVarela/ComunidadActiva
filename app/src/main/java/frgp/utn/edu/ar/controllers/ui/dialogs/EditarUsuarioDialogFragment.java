package frgp.utn.edu.ar.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMAModificarUsuario;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.MailService;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class EditarUsuarioDialogFragment extends DialogFragment {
    private TextView titulo;
    private EditText pass1, pass2, correo, telefono;
    private Usuario selectedUser = null;
    private Usuario loggedInUser = null;
    private Spinner spinTipo;
    LogService logService = new LogService();
    MailService mailService = new MailService();

    SharedPreferencesService sharedPreferencesService = new SharedPreferencesService();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            selectedUser = (Usuario) args.getSerializable("selected_user");
       }
        loggedInUser = sharedPreferencesService.getUsuarioData(getContext());
        System.out.println("selectedUser: "+selectedUser);
        System.out.println("loggedInUser: "+loggedInUser);
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
        spinTipo.setSelection(selectedUser.getTipo().getId()-1);

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

                if(validar_campos()) {
                    modificar_datos_usuario();
                    try {
                        DMAModificarUsuario DMAModifUser = new DMAModificarUsuario(selectedUser);
                        DMAModifUser.execute();
                        if(DMAModifUser.get()){
                            Toast.makeText(getContext(), "Usuario modificado!", Toast.LENGTH_LONG).show();
                            logService.log(loggedInUser.getId(), LogsEnum.MODIFICACION_USUARIO, String.format("Modificaste al usuario %s", selectedUser.getUsername()));
                            mailService.sendMail(selectedUser.getCorreo(), "COMUNIDAD ACTIVA - Modificacion de usuario", "Sus datos han sido modificados por un administrador.");
                        } else {
                            Toast.makeText(getContext(), "No se pudo editar el perfil.", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    dismiss();
                }
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

    private boolean validar_campos(){

        if(!pass1.getText().toString().isEmpty() && !pass2.getText().toString().isEmpty()){
            if(!pass1.getText().toString().equals(pass2.getText().toString())){
                Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                return false;
            }else if (pass1.getText().toString().length() > 20 || pass1.getText().toString().length() < 6){
                Toast.makeText(getContext(), "La clave de tener entre 6 y 20 caracterés", Toast.LENGTH_LONG).show();
                return false;
            }
        }else if (!pass1.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Debe confirmar la contraseña", Toast.LENGTH_LONG).show();
            return false;
        }
        if(correo.getText().toString().length() > 100){
            Toast.makeText(getContext(), "El correo debe tener menos de 100 caracteres", Toast.LENGTH_LONG).show();
            return false;
        }
        if(telefono.getText().toString().length() < 8 || telefono.getText().toString().length() > 15){
            Toast.makeText(getContext(), "Ingrese un teléfono válido.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void modificar_datos_usuario(){
        if(!pass1.getText().toString().isEmpty()){
            selectedUser.setPassword(pass1.getText().toString());
        }
        if(!correo.getText().toString().isEmpty()){
            selectedUser.setCorreo(correo.getText().toString());
        }
        if(!telefono.getText().toString().isEmpty()){
            selectedUser.setTelefono(telefono.getText().toString());
        }
        selectedUser.setTipo(new TipoUsuario(spinTipo.getSelectedItemPosition()+1,spinTipo.getSelectedItem().toString()));

        Log.i("usuario modif", selectedUser.getCorreo()+ " - "+selectedUser.getTipo().getId()+ " | "+ selectedUser.getTipo().getTipo());
    }

}
