package frgp.utn.edu.ar.controllers.ui.fragments;

import static frgp.utn.edu.ar.controllers.R.*;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.EstadoProyecto;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.model.TipoProyecto;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMANuevoProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMASpinnerTiposProyectos;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAUltimoProyectoID;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAUnirseAProyecto;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMAModificarPuntajeUsuario;
import frgp.utn.edu.ar.controllers.ui.activities.HomeActivity;
import frgp.utn.edu.ar.controllers.ui.viewmodels.SharedLocationViewModel;
import frgp.utn.edu.ar.controllers.ui.viewmodels.NuevoProyectoViewModel;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class NuevoProyectoFragment extends Fragment {

    private SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private static final int LOCATION_PERMISSION_REQUEST = 123;
    private SharedLocationViewModel sharedLocationViewModel;
    private NuevoProyectoViewModel mViewModel;
    private Usuario loggedInUser = null;
    private EditText edTitulo, edDesc, edRequerimientos, edContacto, edCupos;
    private Spinner spTipoProyecto;
    private int selectedSpinnerPosition = 0;
    private LogService logService = new LogService();
    public static NuevoProyectoFragment newInstance() {
        return new NuevoProyectoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Shared
        loggedInUser = sharedPreferences.getUsuarioData(getContext());
        if(loggedInUser == null){ /// VALIDA QUE EXISTA USUARIO
            Intent registro = new Intent(getContext(), HomeActivity.class);
            startActivity(registro);
        }
        if (savedInstanceState != null) {
            selectedSpinnerPosition = savedInstanceState.getInt("selectedSpinnerPosition", 0);
        }
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
        if (savedInstanceState == null) {
            DMASpinnerTiposProyectos DMASpinnerTiposProyectos = new DMASpinnerTiposProyectos(spTipoProyecto, getContext(), selectedSpinnerPosition);
            DMASpinnerTiposProyectos.execute();
        }
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
        comportamiento_spinner_tipo();
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
                        nuevoP.setFecha(Calendar.getInstance().getTime());
                        DMANuevoProyecto DMANuevoP = new DMANuevoProyecto(nuevoP);    // LLAMA AL DMA PARA EL GUARDADO EN DB
                        DMANuevoP.execute();
                        /// SI EL PROYECTO SE GUARDA - BUSCA EL ULTIMO ID REGISTRADO (NO FUNCIONA getGeneratedKeys() CON FREESQL)
                        if(DMANuevoP.get()){
                            DMAUltimoProyectoID LastProyectoID = new DMAUltimoProyectoID();
                            LastProyectoID.execute();
                            /// ASIGNA EL ID ENCONTRADO AL PROYECTO (O -1 SI NO LO ENCUENTRA).
                            nuevoP.setId(LastProyectoID.get());
                        }
                        /// SI EL NUMERO ES VALIDO, ASIGNA AL OWNER COMO PRIMER PARTICIPANTE Y CIERRA EL PROCESO
                        if (nuevoP.getId()!=-1){
                            DMAUnirseAProyecto DMAUnirseProyecto = new DMAUnirseAProyecto(loggedInUser.getId(),nuevoP.getId());
                            DMAUnirseProyecto.execute();
                            if(DMAUnirseProyecto.get()){
                                /// SI SE UNE EL OWNER AL PROYECTO - SE AGREGA EL PUNTAJE
                                modificar_puntaje_usuario();
                            }
                            logService.log(loggedInUser.getId(), LogsEnum.CREACION_PROYECTO, String.format("Se creó el proyecto %s", nuevoP.getTitulo()));
                            limpiarCampos();    // LIMPIA CAMPOS DE LOS CONTROLES PARA UN NUEVO INGRESO
                            navigateToBuscarProyecto(); // REGRESA A BUSCAR PROYECTO
                        }else{
                            Toast.makeText(getContext(), "No se pudo crear el proyecto", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (Exception e) {
                   e.printStackTrace();
                }
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

    private void modificar_puntaje_usuario(){
        try {
            DMAModificarPuntajeUsuario DMAPuntaje = new DMAModificarPuntajeUsuario(loggedInUser);
            DMAPuntaje.execute();
            if(DMAPuntaje.get()){
                sharedPreferences.saveUsuarioData(getContext(), loggedInUser);
                Toast.makeText(getContext(),"Proyecto creado", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(),"Proyecto creado con errores", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void comportamiento_spinner_tipo(){
        spTipoProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Guarda la posición seleccionada en la variable
                selectedSpinnerPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No es necesario hacer nada
            }
        });
    }

    private Proyecto cargarDatos(){
        int puntaje = loggedInUser.getPuntuacion() + 15;
        loggedInUser.setPuntuacion(puntaje);
        Proyecto nuevoP = new Proyecto();
        nuevoP.setContacto(edContacto.getText().toString());
        nuevoP.setCupo(Integer.parseInt(edCupos.getText().toString()));
        nuevoP.setDescripcion(edDesc.getText().toString());
        nuevoP.setTipo(new TipoProyecto(spTipoProyecto.getSelectedItemPosition()+1, spTipoProyecto.getSelectedItem().toString()));
        nuevoP.setRequerimientos(edRequerimientos.getText().toString());
        nuevoP.setTitulo(edTitulo.getText().toString());
        nuevoP.setLatitud(sharedLocationViewModel.getLatitude());
        nuevoP.setLongitud(sharedLocationViewModel.getLongitude());
        nuevoP.setOwner(loggedInUser);
        nuevoP.setFecha(new Date(System.currentTimeMillis()));
        nuevoP.setEstado(new EstadoProyecto(1,"ABIERTO"));
        return nuevoP;
    }

    private boolean validarDatosProyecto(){
        if(edTitulo.getText().toString().trim().isEmpty()){
            Toast.makeText(this.getContext(), "Ingresa un titulo para tu proyecto", Toast.LENGTH_LONG).show();
            return false;}
        if(edTitulo.getText().toString().length()>50){
            Toast.makeText(this.getContext(), "El titulo es muy largo!", Toast.LENGTH_LONG).show();
            return false;}
        if(edDesc.getText().toString().trim().isEmpty()){
            Toast.makeText(this.getContext(), "Ingresa una descripción para tu proyecto", Toast.LENGTH_LONG).show();
            return false;}
        if(edDesc.getText().toString().length()>250){
            Toast.makeText(this.getContext(), "La descripción es muy larga!", Toast.LENGTH_LONG).show();
            return false;}
        if(edRequerimientos.getText().toString().trim().isEmpty()){
            Toast.makeText(this.getContext(), "Ingresa un requerimiento o más para tu proyecto", Toast.LENGTH_LONG).show();
            return false;}
        if(edRequerimientos.getText().toString().length()>150){
            Toast.makeText(this.getContext(), "Los requerimientos son muy largos!", Toast.LENGTH_LONG).show();
            return false;}
        if(edContacto.getText().toString().trim().isEmpty()){
            Toast.makeText(this.getContext(), "Ingresa un telefono de contacto para tu proyecto", Toast.LENGTH_LONG).show();
            return false;}
        if(edContacto.getText().toString().length()>50){
            Toast.makeText(this.getContext(), "El texto de contacto es muy largo!", Toast.LENGTH_LONG).show();
            return false;}
        if(edCupos.getText().toString().trim().isEmpty()) {
            Toast.makeText(this.getContext(), "Ingresa un valor para indicar la cantidad máxima de participantes", Toast.LENGTH_LONG).show();
        }else if (Integer.parseInt(edCupos.getText().toString()) <= 0 || Integer.parseInt(edCupos.getText().toString()) >= 51) {
            Toast.makeText(this.getContext(), "El cupo debe ser un número entre 1 y 50", Toast.LENGTH_LONG).show();
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

    private void navigateToBuscarProyecto(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nuevoProyecto_to_buscarProyectos);
    }
}