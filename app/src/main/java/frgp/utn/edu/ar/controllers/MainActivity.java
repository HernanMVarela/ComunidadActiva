package frgp.utn.edu.ar.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import frgp.utn.edu.ar.controllers.ui.home_admin.AdminActivity;
import frgp.utn.edu.ar.controllers.ui.home_moderador.ModeradorActivity;
import frgp.utn.edu.ar.controllers.ui.home_vecino.VecinoActivity;
import frgp.utn.edu.ar.controllers.ui.recupero_contraseña.RecuperoPassActivity;
import frgp.utn.edu.ar.controllers.ui.registro.RegistroActivity;

public class MainActivity extends AppCompatActivity {

    public EditText etNombre, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombre = (EditText) findViewById(R.id.etNombre);
        etPassword = (EditText) findViewById(R.id.etPassword);
    }

    public void IrRegistro(View view){
        Intent registro = new Intent(this, RegistroActivity.class);
        startActivity(registro);
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
}