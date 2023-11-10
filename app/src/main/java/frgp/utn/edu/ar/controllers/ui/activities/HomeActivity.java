package frgp.utn.edu.ar.controllers.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.databinding.ActivityHomeBinding;
import frgp.utn.edu.ar.controllers.ui.dialogs.CerrarSesionDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.UsuarioViewModel;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class HomeActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private NavController navController;
    private TextView tvNavUsername,tvNavUserMail;
    private Usuario usuario;
    private LogService logger = new LogService();
    private SharedPreferencesService sharedPreferences = new SharedPreferencesService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_actividad_rec, R.id.nav_notificaciones,R.id.nav_cambiarpassword, R.id.nav_cerrarsesion)
                .setOpenableLayout(drawer)
                .build();

        ///CIERRE SESION
        navigationView.getMenu().findItem(R.id.nav_cerrarsesion).setOnMenuItemClickListener(menuItem -> {

        Bundle bundle = new Bundle();
        bundle.putSerializable("logged_in_user", usuario);
        CerrarSesionDialogFragment dialogFragment = new CerrarSesionDialogFragment();
        dialogFragment.setArguments(bundle); // Establece el Bundle como argumento
        dialogFragment.show(getSupportFragmentManager(), "layout_cerrar_sesion");
        return true;
        });

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        ///RECUPERO SHARED PREFERENCES
        View headerView = navigationView.getHeaderView(0);
        tvNavUsername = headerView.findViewById(R.id.tvNavBarUserName);
        tvNavUserMail = headerView.findViewById(R.id.tvNavBarMail);
        usuario = sharedPreferences.getUsuarioData(this);
        tvNavUsername.setText(usuario.getUsername());
        tvNavUserMail.setText(usuario.getCorreo());
        if(usuario==null){
            Log.e("ERROR USER", "NO HAY USAURIO");
        }
        UsuarioViewModel userViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        userViewModel.setUsuario(usuario);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menubar_accesibilidad) {
            navController.navigate(R.id.action_nav_home_to_nav_accesibilidad);
            return true;
        } else if (id == R.id.menubar_ayuda) {
            navController.navigate(R.id.action_nav_home_to_nav_ayuda);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Usuario getUsuario(){
        return usuario;
    }
}