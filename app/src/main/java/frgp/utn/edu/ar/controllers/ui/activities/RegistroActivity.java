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

public class RegistroActivity extends AppCompatActivity implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private EditText nombre, apellido, userName, fechaNacimiento, telefono, correo, password, password2;
    private Calendar mCalendar;
    private SimpleDateFormat mFormat;
    private Spinner spinnerCategoriaUSer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        spinnerCategoriaUSer = (Spinner)findViewById(R.id.spnCategoriaUserRegistro);
        String [] opciones = {"VECINO", "MODERADOR", "ADMINISTRADOR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,opciones);
        spinnerCategoriaUSer.setAdapter(adapter);

        nombre = (EditText)findViewById(R.id.etNombreRegistro);
        apellido = (EditText)findViewById(R.id.etApellidoRegistro);
        userName = (EditText)findViewById(R.id.etUserNameRegistro);
        fechaNacimiento = (EditText)findViewById(R.id.etFechaNacimientoRegistro);
        telefono = (EditText)findViewById(R.id.etTelefonoRegistro);
        correo = (EditText)findViewById(R.id.etCorreoRegistro);
        password = (EditText)findViewById(R.id.etPassUnoRegistro);
        password2 = (EditText)findViewById(R.id.etPassDosRegistro);

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

        startActivity(intent);
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

        //CHECK FECHA NACIMIENTO NO FUTURA
        if (mCalendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
            Toast.makeText(this, "La fecha de nacimiento no puede ser futura", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


}