package frgp.utn.edu.ar.controllers.ui.fragments;

import static frgp.utn.edu.ar.controllers.R.*;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;

import frgp.utn.edu.ar.controllers.DAOImpl.Proyecto.DMANuevoProyecto;
import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.EstadoProyecto;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.model.TipoProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMASpinnerTiposProyectos;
import frgp.utn.edu.ar.controllers.ui.viewmodels.SharedLocationViewModel;
import frgp.utn.edu.ar.controllers.ui.viewmodels.NuevoProyectoViewModel;

public class NuevoProyectoFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST = 123;
    private SharedLocationViewModel sharedLocationViewModel;
    private NuevoProyectoViewModel mViewModel;
    private EditText edTitulo, edDesc, edRequerimientos, edContacto, edCupos;
    private Spinner spTipoProyecto;
    public static NuevoProyectoFragment newInstance() {
        return new NuevoProyectoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_nuevo_proyecto, container, false);

        edTitulo = view.findViewById(id.edTituloP);
        edDesc = view.findViewById(id.edDescP);
        edRequerimientos = view.findViewById(id.edRequerimientosP);
        edContacto = view.findViewById(id.edContactoP2);
        edCupos = view.findViewById(id.edCupoP2);

        spTipoProyecto = view.findViewById(id.spTipoP);
        DMASpinnerTiposProyectos tiposPro = new DMASpinnerTiposProyectos(spTipoProyecto, getContext());
        tiposPro.execute();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NuevoProyectoViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Iniciaza textos, botones, spinner y proyecto.

        Button btnUbicacion = view.findViewById(id.btnUbicacionP);
        Button btnCrearProyecto = view.findViewById(id.btnCrearInformeModerador);

        btnCrearProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                // INICIALIZA EL sharedLocationViewModel, QUE PERMITE COMPARTIR LA UBICACION SELECCIONADA ENTRE FRAGMENTOS
                sharedLocationViewModel = new ViewModelProvider(requireActivity()).get(SharedLocationViewModel.class);
                // VALIDA CAMPOS EN PANTALLA
                if(validarDatosProyecto()){
                    // CARGA DATOS DE LOS CONTROLES AL NUEVO PROYECTO
                    Proyecto nuevoP = cargarDatos();
                    DMANuevoProyecto DMANuevoP = new DMANuevoProyecto(nuevoP,v.getContext());    // LLAMA AL DMA PARA EL GUARDADO EN DB
                    DMANuevoP.execute();    // COMENTADO PARA NO IMPACTAR EN DB - MODIFICACION DE TABLAS PENDIENTE
                    limpiarCampos();    // LIMPIA CAMPOS DE LOS CONTROLES PARA UN NUEVO INGRESO
                    Log.i("Existoso","Se guardo bien el proyecto");
                }
                else {
                Log.e("ERROR","No se guardo bien el proyecto");
                }
            }
            catch (Error e) {
                Log.e("Error", e.toString());}
            }
        });
        btnUbicacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Verificar si se tiene permiso de ubicación
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Navega al fragmento de ubicación
                    navigateToLocationFragment();
                } else {
                    // Si no tiene el permiso, se pide al usuario
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
                }
            }
        });
    }

    private Proyecto cargarDatos(){
        Proyecto nuevoP = new Proyecto();
        nuevoP.setContacto(edContacto.getText().toString());
        nuevoP.setCupo(Integer.parseInt(edCupos.getText().toString()));
        nuevoP.setDescripcion(edDesc.getText().toString());
        nuevoP.setTipo(new TipoProyecto(spTipoProyecto.getSelectedItemPosition()+1, spTipoProyecto.getSelectedItem().toString()));
        nuevoP.setRequerimientos(edRequerimientos.getText().toString());
        nuevoP.setTitulo(edTitulo.getText().toString());
        nuevoP.setLatitud(sharedLocationViewModel.getLatitude());
        nuevoP.setLongitud(sharedLocationViewModel.getLongitude());
        nuevoP.setOwner(null);
        nuevoP.setFecha(new Date(System.currentTimeMillis()));
        nuevoP.setEstado(new EstadoProyecto(1,"ABIERTO"));
        return nuevoP;
    }

    private boolean validarDatosProyecto(){
        if(edTitulo.getText().toString().trim().isEmpty()){
            Toast.makeText(this.getContext(), "Ingresa un titulo para tu proyecto", Toast.LENGTH_LONG).show();
            return false;}
        if(edDesc.getText().toString().trim().isEmpty()){
            Toast.makeText(this.getContext(), "Ingresa una descripción para tu proyecto", Toast.LENGTH_LONG).show();
            return false;}
        if(edRequerimientos.getText().toString().trim().isEmpty()){
            Toast.makeText(this.getContext(), "Ingresa un requerimiento o más para tu proyecto", Toast.LENGTH_LONG).show();
            return false;}
        if(edContacto.getText().toString().trim().isEmpty()){
            Toast.makeText(this.getContext(), "Ingresa un telefono de contacto para tu proyecto", Toast.LENGTH_LONG).show();
            return false;}
        if(edCupos.getText().toString().trim().isEmpty()) {
            Toast.makeText(this.getContext(), "Ingresa un valor para indicar la cantidad máxima de participantes", Toast.LENGTH_LONG).show();
        }else if (Integer.parseInt(edCupos.getText().toString()) <= 0) {
            Toast.makeText(this.getContext(), "Ingresa un cupo mayor a 0 para tu proyecto", Toast.LENGTH_LONG).show();
            return false;
        }
        if (sharedLocationViewModel.getLatitude() == 0 || sharedLocationViewModel.getLongitude() == 0) {
            Toast.makeText(this.getContext(), "Ingresa una ubicación.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public void limpiarCampos (){
        edTitulo.setText("");
        edContacto.setText("");
        edCupos.setText("");
        edDesc.setText("");
        edRequerimientos.setText("");
        sharedLocationViewModel.setLatitude(0);
        sharedLocationViewModel.setLongitude(0);
    }
    private void navigateToLocationFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(id.action_nav_nuevo_proyecto_to_nav_ubicacion);
    }
}