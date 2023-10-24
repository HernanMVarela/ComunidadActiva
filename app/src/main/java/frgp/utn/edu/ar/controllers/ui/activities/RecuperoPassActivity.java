package frgp.utn.edu.ar.controllers.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.repository.usuario.UsuarioRepository;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.MailService;
import frgp.utn.edu.ar.controllers.utils.Utils;


public class RecuperoPassActivity extends AppCompatActivity {

    LogService logger = new LogService();
    MailService mailService = new MailService();
    UsuarioRepository usuarioRepository = new UsuarioRepository();
    private Usuario usuario;

    private EditText correo, codigo, pass, pass2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recupero_pass);

        correo = findViewById(R.id.etCorreoRecupero);
        codigo = findViewById(R.id.etCodigoRecupero);
        pass = findViewById(R.id.etPassRecupero);
        pass2 = findViewById(R.id.etPassConfirmarRecupero);
    }

    public void obtenerCodigoRecupero(View view){
        String correo = this.correo.getText().toString();

        if(correo.isEmpty()) {
            Toast.makeText(this, "Debe ingresar un correo", Toast.LENGTH_LONG).show();
            return;
        }

        if (!correo.contains("@")) {
            Toast.makeText(this, "Ingrese una direccion de mail valida", Toast.LENGTH_LONG).show();
            return;
        }

        usuario = usuarioRepository.getUserByMail(correo);

        System.out.println("Usuario: " + usuario);

        if(usuario != null){
            usuario.setCodigo_recuperacion(Utils.generateToken());
            usuarioRepository.modificarUsuario(usuario);
            mailService.sendMail(usuario.getCorreo(), "Codigo de recuperacion", "Su codigo de recuperacion es: " + usuario.getCodigo_recuperacion());
            logger.log(usuario.getId(), LogsEnum.RECUPERO_PASS, "Se envio el codigo de recuperacion al correo: " + usuario.getCorreo());
            Toast.makeText(this, "Revise su correo electronico para obtener el codigo de recuperacion", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "No se encontro ningun usuario con el correo ingresado", Toast.LENGTH_LONG).show();
    }

    public void cambiarPass(View view) {

        if(!isFormValid()) {
            return;
        }

        if(!isDataValid()) {
            return;
        }

        usuario.setPassword(pass.getText().toString());
        usuario.setCodigo_recuperacion(null);
        usuarioRepository.modificarUsuario(usuario);
        mailService.sendMail(usuario.getCorreo(), "Cambio de contraseña", "Su contraseña fue cambiada correctamente");
        logger.log(usuario.getId(), LogsEnum.CAMBIO_PASS, "Se cambio la contraseña del usuario: " + usuario.getUsername());
        Toast.makeText(this, "Se cambio la contraseña correctamente", Toast.LENGTH_LONG).show();
        finish();
    }

    public boolean isFormValid() {
        if(codigo.getText().toString().isEmpty()
        || pass.getText().toString().isEmpty()
        || pass2.getText().toString().isEmpty()) {
            Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!pass.getText().toString().equals(pass2.getText().toString())) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            return false;
        }

        if (pass.getText().toString().length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean isDataValid() {
        if(!codigo.getText().toString().equals(usuario.getCodigo_recuperacion())) {
            Toast.makeText(this, "El codigo ingresado no es valido", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}