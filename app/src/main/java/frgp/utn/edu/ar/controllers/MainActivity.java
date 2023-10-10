package frgp.utn.edu.ar.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import frgp.utn.edu.ar.controllers.ui.homeAdmin.AdminActivity;
import frgp.utn.edu.ar.controllers.ui.homeModerador.ModeradorActivity;
import frgp.utn.edu.ar.controllers.ui.homeVecino.VecinoActivity;
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