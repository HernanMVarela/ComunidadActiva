package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;

import frgp.utn.edu.ar.controllers.data.model.EstadoReporte;
import frgp.utn.edu.ar.controllers.data.model.TipoReporte;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMAGuardarReporte;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMASpinnerTiposReporte;
import frgp.utn.edu.ar.controllers.ui.activities.HomeActivity;
import frgp.utn.edu.ar.controllers.ui.viewmodels.NuevoReporteViewModel;
import frgp.utn.edu.ar.controllers.ui.viewmodels.SharedLocationViewModel;
import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class NuevoReporteFragment extends Fragment {
    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int LOCATION_PERMISSION_REQUEST = 123;
    private NuevoReporteViewModel mViewModel;
    private Usuario loggedInUser = null;
    SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private Bitmap imagenCapturada;
    private SharedLocationViewModel sharedLocationViewModel;
    private Spinner spinTipoReporte;
    private int selectedSpinnerPosition = 0;
    private EditText titulo, descripcion;
    public static NuevoReporteFragment newInstance() {
        return new NuevoReporteFragment();
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
        View view = inflater.inflate(R.layout.fragment_nuevo_reporte, container, false);
        if(getActivity() instanceof HomeActivity){
            ((HomeActivity) getActivity()).botonmensaje.hide();
        }
        titulo = view.findViewById(R.id.edTituloReporte);
        descripcion = view.findViewById(R.id.edDescripcionReporte);
        spinTipoReporte = view.findViewById(R.id.spnTiposReporte);

        if (savedInstanceState == null) {
            DMASpinnerTiposReporte dataActivityTiposReporte = new DMASpinnerTiposReporte(spinTipoReporte, getContext(),selectedSpinnerPosition);
            dataActivityTiposReporte.execute();
        }

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // INICIAIZA BOTONES DEL FRAGMENTO
        Button bCamara = view.findViewById(R.id.btnCamara);
        Button bUbicacion = view.findViewById(R.id.btnUbicacion);
        Button bCrearReporte = view.findViewById(R.id.btnCrearReporte);

        bCamara.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Verificar si tiene permiso de la cámara
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // SI TIENE PERMISOS, REDIRIGE A LA VISTA DE LA CÁMARA
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                } else {
                    // Si no tiene el permiso, se pide al usuario
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PIC_REQUEST);
                }
            }
        });
        spinTipoReporte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Guarda la posición seleccionada en la variable
                selectedSpinnerPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No es necesario hacer nada aquí en este caso
            }
        });
        bUbicacion.setOnClickListener(new View.OnClickListener() {
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
        bCrearReporte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    // INICIALIZA EL sharedLocationViewModel, QUE PERMITE COMPARTIR LA UBICACION SELECCIONADA ENTRE FRAGMENTOS
                    sharedLocationViewModel = new ViewModelProvider(requireActivity()).get(SharedLocationViewModel.class);
                    if(checkFormValid()){ // VALIDA CAMPOS ANTES DE GUARDAR REPORTE
                        Reporte nuevo = cargarDatos(); // CARGA LOS DATOS EN EL NUEVO REPORTE
                        DMAGuardarReporte DMAGuardar = new DMAGuardarReporte(nuevo,v.getContext());
                        DMAGuardar.execute(); // GUARDA EL REPORTE EN DB
                        limpiarCampos(); // LIMPIA CAMPOS DE PANTALLA
                        navigateToBuscarReporte(); // REGRESA A BUSCARREPORTES
                    }
                }catch (Exception e){
                    Log.e("Error", e.toString());
                }
            }
        });
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedSpinnerPosition", selectedSpinnerPosition);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // VALIDA QUE LA ACCION SE HAYA COMPLETADO CORRECTAMENTE
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            // La imagen se capturó exitosamente
            Bundle extras = data.getExtras();
            imagenCapturada = (Bitmap) extras.get("data");
            // Referencia al ImageView - prueba de imagen
            ImageView imgViewFotoTomada = getView().findViewById(R.id.imgViewFotoTomada);
            // Modificación de tamaño - prueba de imagen
            Bitmap imagenRedimensionada = Bitmap.createScaledBitmap(imagenCapturada, imagenCapturada.getWidth()*2, imagenCapturada.getHeight()*2, true);
            // Configura la imagen capturada en el ImageView
            imgViewFotoTomada.setImageBitmap(imagenRedimensionada);
        }
    }

    private void navigateToLocationFragment() {
        // NAVEGA AL FRAGMENTO DE UBICACIÓN
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_reporte_to_nav_ubicacion);
    }

    private void returnToHome(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.popBackStack();
    }

    private void navigateToBuscarReporte(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nuevoReporte_to_buscarReportes);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NuevoReporteViewModel.class);
        // TODO: Use the ViewModel
    }

    /// VALIDACIONES DE CAMPOS
    private boolean checkFormValid() {
        if (titulo.getText().toString().trim().isEmpty()) {
            Toast.makeText(this.getContext(), "Debes poner un título al reporte.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (descripcion.getText().toString().trim().isEmpty()) {
            Toast.makeText(this.getContext(), "Debes dar una descripcion del problema.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (spinTipoReporte.getSelectedItemPosition()+1 == 0) {
            Toast.makeText(this.getContext(), "No has seleccionado un tipo de reporte.", Toast.LENGTH_LONG).show();
            return false;
        } else if (spinTipoReporte.getSelectedItem().toString().trim().isEmpty()){
            Toast.makeText(this.getContext(), "Tipo de reporte no seleccionado.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (sharedLocationViewModel.getLatitude() == 0 || sharedLocationViewModel.getLongitude() == 0) {
            Toast.makeText(this.getContext(), "No has seleccionado una ubicación.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (imagenCapturada == null) {
            Toast.makeText(this.getContext(), "No has cargado una imagen.", Toast.LENGTH_LONG).show();
            return false;
        } else if (imagenCapturada.getWidth() < 1 || imagenCapturada.getHeight() < 1){
            Toast.makeText(this.getContext(), "La imagen es inválida.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private Reporte cargarDatos(){
        // Toma las coordenadas desde el ViewModel compartido con el UbicacionFragment
        double latitude = sharedLocationViewModel.getLatitude();
        double longitude = sharedLocationViewModel.getLongitude();
        // Crea el objeto Reporte y carga los datos
        Reporte nuevoReporte = new Reporte();
        nuevoReporte.setLatitud(latitude);
        nuevoReporte.setLongitud(longitude);
        nuevoReporte.setTitulo(titulo.getText().toString());
        nuevoReporte.setDescripcion(descripcion.getText().toString());
        nuevoReporte.setEstado(new EstadoReporte(1,"ABIERTO")); // INICIA CON ESTADO ABIERTO
        nuevoReporte.setTipo(new TipoReporte(spinTipoReporte.getSelectedItemPosition()+1, spinTipoReporte.getSelectedItem().toString()));
        nuevoReporte.setImagen(imagenCapturada); // TRAE IMAGEN CAPTURADA
        nuevoReporte.setPuntaje(0); // INICIA CON PUNTAJE 0
        nuevoReporte.setFecha(new Date(System.currentTimeMillis())); // FECHA ACTUAL
        nuevoReporte.setOwner(loggedInUser); // USUARIO LOGUEADO

        return nuevoReporte;
    }
    private void limpiarCampos(){
        // LIMPIA CAMPOS DE TEXTO Y SETEA A 0 TODAS LAS VARIABLES
        titulo.setText("");
        descripcion.setText("");
        sharedLocationViewModel.setLatitude(0);
        sharedLocationViewModel.setLongitude(0);
        imagenCapturada = null;
    }

}