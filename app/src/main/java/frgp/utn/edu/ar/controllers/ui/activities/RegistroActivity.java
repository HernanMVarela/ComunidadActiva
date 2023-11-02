package frgp.utn.edu.ar.controllers.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.repository.usuario.UsuarioRepository;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.MailService;

public class RegistroActivity extends AppCompatActivity implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {

    LogService logger = new LogService();
    MailService mailService = new MailService();
    UsuarioRepository usuarioRepository = new UsuarioRepository();
    private EditText nombre, apellido, userName, fechaNacimiento, telefono, correo, password, password2;
    private Calendar mCalendar;
    private SimpleDateFormat mFormat;
    private Spinner spinnerCategoriaUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        spinnerCategoriaUser = (Spinner)findViewById(R.id.spnCategoriaUserRegistro);
        String [] opciones = {"VECINO", "MODERADOR", "ADMINISTRADOR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,opciones);
        spinnerCategoriaUser.setAdapter(adapter);

        nombre = findViewById(R.id.etNombreRegistro);
        apellido = findViewById(R.id.etApellidoRegistro);
        userName = findViewById(R.id.etUserName);
        fechaNacimiento = findViewById(R.id.etFechaNacimientoRegistro);
        telefono = findViewById(R.id.etTelefonoRegistro);
        correo = findViewById(R.id.etCorreoRegistro);
        password = findViewById(R.id.etPassUnoRegistro);
        password2 = findViewById(R.id.etPassDosRegistro);

        fechaNacimiento.setOnFocusChangeListener(this);
        fechaNacimiento.setOnClickListener(this);
        mFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            showPicker(view);
        }
    }

    @Override
    public void onClick(View view) {
        showPicker(view);
    }

    private void showPicker(View view) {
        if (mCalendar == null)
            mCalendar = Calendar.getInstance();

        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);

        new DatePickerDialog(view.getContext(), this, year, month, day).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        this.fechaNacimiento.setText(mFormat.format(mCalendar.getTime()));
    }

    public void registrarUsuario(View view){
        android.content.Intent intent = new android.content.Intent(this, MainActivity.class);

        if(!isFormValid()){
            return;
        }

        if(!isDataValid(view)){
            return;
        }

        if(usuarioRepository.cargarUsuario(crearUsuario(), view.getContext())) {
            Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_LONG).show();
            Usuario usuario = usuarioRepository.getUserByUserName(userName.getText().toString());
            logger.log(usuario.getId(), LogsEnum.REGISTRO_USUARIO, "Se registro el usuario " + usuario.getUsername());
            mailService.sendMail(usuario.getCorreo(), "BIENVENIDO A COMUNIDAD ACTIVA", String.format("Hola %s, tu usuario con el rol %s fue creado correctamente, C", usuario.getUsername(), usuario.getTipo().getTipo()));
            startActivity(intent);
        }else{
            Toast.makeText(this, "Error al crear el usuario", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isFormValid() {
        //CHECK FORM VACIO
        if (nombre.getText().toString().isEmpty()
         || apellido.getText().toString().isEmpty()
         || userName.getText().toString().isEmpty()
         || fechaNacimiento.getText().toString().isEmpty()
         || telefono.getText().toString().isEmpty()
         || correo.getText().toString().isEmpty()
         || password.getText().toString().isEmpty()
         || password2.getText().toString().isEmpty()) {
            Toast.makeText(this, "No se puede dejar campos vacios", Toast.LENGTH_SHORT).show();
            return false;
        }

        //CHECK PASSWORDS MATCH
        if (!password.getText().toString().equals(password2.getText().toString())) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        //CHECK PASSWORD LENGTH
        if (password.getText().toString().length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        //CHECK USERNAME LENGTH
        if (userName.getText().toString().length() < 6) {
            Toast.makeText(this, "El nombre de usuario debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        //CHECK USERNAME VALID
        if (userName.getText().toString().contains(" ")) {
            Toast.makeText(this, "El nombre de usuario no puede contener espacios", Toast.LENGTH_LONG).show();
            return false;
        }

        //CHECK MAIL VALID
        if (!correo.getText().toString().contains("@")) {
            Toast.makeText(this, "Ingrese una direccion de mail valida", Toast.LENGTH_LONG).show();
            return false;
        }

        //CHECH FECHA VALID
        try {
            String date = fechaNacimiento.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            sdf.parse(date);
        } catch (Exception e) {
            Toast.makeText(this, "Ingrese una fecha de nacimiento valida", Toast.LENGTH_LONG).show();
            return false;
        }

        //CHECK FECHA NACIMIENTO NO FUTURA
        if (mCalendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
            Toast.makeText(this, "La fecha de nacimiento no puede ser futura", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public boolean isDataValid(View view) {

        //CHECK EXISTENCIA USERNAME
        if(usuarioRepository.getUserByUserName(userName.getText().toString()) != null) {
            Toast.makeText(this, "El nombre de usuario ya existe", Toast.LENGTH_LONG).show();
            return false;
        }

        //CHECK EXISTENCIA MAIL
        if(usuarioRepository.getUserByMail(correo.getText().toString()) != null) {
            Toast.makeText(this, "El correo ya esta registrado", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public Usuario crearUsuario(){
        Usuario nuevo = new Usuario();
        nuevo.setUsername(userName.getText().toString());
        nuevo.setPassword(password.getText().toString());
        nuevo.setPuntuacion(0);
        nuevo.setNombre(nombre.getText().toString());
        nuevo.setApellido(apellido.getText().toString());
        nuevo.setTelefono(telefono.getText().toString());
        nuevo.setCorreo(correo.getText().toString());
        nuevo.setFecha_nac(mCalendar.getTime());
        nuevo.setFecha_alta(Calendar.getInstance().getTime());

        int tipoUsuarioId = spinnerCategoriaUser.getSelectedItemPosition()+1;
        String tipoUsuarioString = spinnerCategoriaUser.getSelectedItem().toString();
        nuevo.setTipo(new TipoUsuario(tipoUsuarioId, tipoUsuarioString));

        int estadoUsuarioId;
        String estadoUsuarioString;

        if(tipoUsuarioId == 1){
            estadoUsuarioId = 1;
            estadoUsuarioString = "ACTIVO";
        }else{
            estadoUsuarioId = 2;
            estadoUsuarioString = "INACTIVO";
        }

        nuevo.setEstado(new EstadoUsuario(estadoUsuarioId, estadoUsuarioString));

        nuevo.setCodigo_recuperacion(null);
        nuevo.setFecha_bloqueo(null);

        return nuevo;
    }
}