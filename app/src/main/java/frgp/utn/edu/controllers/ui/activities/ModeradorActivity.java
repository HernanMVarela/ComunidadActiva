package frgp.utn.edu.controllers.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.databinding.ActivityModeradorBinding;


public class ModeradorActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityModeradorBinding binding;
    public FloatingActionButton botonmensaje;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderador);

        binding = ActivityModeradorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        botonmensaje = findViewById(R.id.fab);
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        /*
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_actividad_rec, R.id.nav_notificaciones,R.id.nav_cambiarpassword, R.id.nav_cerrarsesion)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        */
    }


}