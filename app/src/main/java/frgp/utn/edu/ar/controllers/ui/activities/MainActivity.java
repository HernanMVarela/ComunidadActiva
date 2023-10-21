package frgp.utn.edu.ar.controllers.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.repository.usuario.UsuarioRepository;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.MailService;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class MainActivity extends AppCompatActivity {
    LogService logger = new LogService();
    MailService mailService = new MailService();
    UsuarioRepository usuarioRepository = new UsuarioRepository();

    SharedPreferencesService sharedPreferences = new SharedPreferencesService();

    public Usuario usuario;
    public int loginAttemps = 0;
    public EditText etNombre, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etNombre = findViewById(R.id.etNombre);
        etPassword = findViewById(R.id.etPassword);
        checkActiveUser();
    }

    public void iniciarSesion(View view){
        if(!isFormValid()) {
            return;
        }

        if(!isDataValid()) {
            return;
        }

        if(loginAttemps > 3) {
            Toast.makeText(this, "Exedio el numero de intentos.", Toast.LENGTH_LONG).show();
            bloqueoDesbloqueoUser("BLOQUEO");
            return;
        }

        sharedPreferences.saveUsuarioData(this, usuario);
        etNombre.setText("");
        etPassword.setText("");

        switch (usuario.getTipo().getTipo()) {
            case "VECINO":
                IrVecino(view);
                break;
            case "MODERADOR":
                IrModerador(view);
                break;
            case "ADMINISTRADOR":
                IrAdministrador(view);
                break;
        }
    }

    public void IrRegistro(View view){
        Intent registro = new Intent(this, RegistroActivity.class);
        startActivity(registro);
    }

    public boolean isFormValid() {
        //CHECK FORM VACIO
        if (etNombre.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean isDataValid() {

        usuario = usuarioRepository.loginUsuario(etNombre.getText().toString(), etPassword.getText().toString());

        //CHECK EXISTENCIA USER
        if(usuario == null) {
            Toast.makeText(this, "Mail o Contraseña Incorrectos", Toast.LENGTH_LONG).show();
            loginAttemps++;
            return false;
        }

        //CHECK ESTADO USER
        if(usuario.getEstado().getEstado().equals("SUSPENDIDO")) {
            Toast.makeText(this, "Usuario suspendido. Contacte con el adminsitrador", Toast.LENGTH_LONG).show();
            return false;
        }

        if(usuario.getEstado().getEstado().equals("BLOQUEADO")) {
            //Check if bloqueo date is more than two days old
            if(!(usuario.getFecha_bloqueo().getTime() + 172800000 < System.currentTimeMillis())) {
                Toast.makeText(this, "Usuario bloqueado intente nuevamente mas tarde.", Toast.LENGTH_LONG).show();
                return false;
            }
            bloqueoDesbloqueoUser("DESBLOQUEO");
            return true;
        }
        return true;
    }

    public void bloqueoDesbloqueoUser(String condition){
        Usuario usuarioBloq = usuarioRepository.getUserByUserName(etNombre.getText().toString());

        switch (condition){
            case "BLOQUEO":
                if(usuarioBloq != null) {
                    usuarioBloq.setEstado(new EstadoUsuario(4, "BLOQUEADO"));
                    usuarioBloq.setFecha_bloqueo(new java.sql.Date(System.currentTimeMillis()));
                    usuarioRepository.modificarUsuario(usuarioBloq);
                    mailService.sendMail(usuarioBloq.getCorreo(), "Bloqueo de Usuario", "Su usuario ha sido bloqueado por intentos maximos de login");
                    logger.log(usuarioBloq.getId(), LogsEnum.BLOQUEO_USUARIO, String.format("Usuario %s bloqueado por intentos maximos de login", usuarioBloq.getUsername()));
                }
                Toast.makeText(this, "Usuario bloqueado intente nuevamente mas tarde.", Toast.LENGTH_LONG).show();
                loginAttemps = 0;
                break;
            case "DESBLOQUEO":
                if(usuarioBloq != null) {
                    usuarioBloq.setEstado(new EstadoUsuario(1, "ACTIVO"));
                    usuarioRepository.modificarUsuario(usuarioBloq);
                    mailService.sendMail(usuarioBloq.getCorreo(), "Desbloqueo de Usuario", "Su usuario ha sido desbloqueado");
                    logger.log(usuarioBloq.getId(), LogsEnum.DESBLOQUEO_USUARIO, String.format("Usuario %s desbloqueado", usuarioBloq.getUsername()));
                }
                loginAttemps = 0;
                break;
        }
    }

    public void IrRecuperoPass(View view){
        Intent registro = new Intent(this, RecuperoPassActivity.class);
        startActivity(registro);
    }

    public void IrVecino(View view){
        Intent registro = new Intent(this, VecinoActivity.class);
        startActivity(registro);
    }

    public void IrModerador(View view){
        Intent registro = new Intent(this, ModeradorActivity.class);
        startActivity(registro);
    }

    public void IrAdministrador(View view){
        Intent registro = new Intent(this, AdminActivity.class);
        startActivity(registro);
    }

    public void checkActiveUser() {
        Usuario usuario = sharedPreferences.getUsuarioData(this);
        if(usuario != null) {
            switch (usuario.getTipo().getTipo()) {
                case "VECINO":
                    IrVecino(null);
                    break;
                case "MODERADOR":
                    IrModerador(null);
                    break;
                case "ADMINISTRADOR":
                    IrAdministrador(null);
                    break;
            }
        }
    }
}