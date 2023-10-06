package frgp.utn.edu.ar.controllers.ui.nuevo_reporte;

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
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import frgp.utn.edu.ar.adapters.SharedLocationViewModel;
import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.ui.ubicacion.UbicacionFragment;
import frgp.utn.edu.ar.entidades.Reporte;

public class NuevoReporteFragment extends Fragment {
    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int LOCATION_PERMISSION_REQUEST = 123;
    private NuevoReporteViewModel mViewModel;
    private Bitmap imagenCapturada;
    private SharedLocationViewModel sharedLocationViewModel;
    private Reporte nuevo;

    public static NuevoReporteFragment newInstance() {
        return new NuevoReporteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nuevo_reporte, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // INICIALIZA EL sharedLocationViewModel, QUE PERMITE COMPARTIR LA UBICACION SELECCIONADA ENTRE FRAGMENTOS
        sharedLocationViewModel = new ViewModelProvider(requireActivity()).get(SharedLocationViewModel.class);

        // INICIAIZA BOTONES DEL FRAGMENTO
        Button bCamara = view.findViewById(R.id.btnCamara);
        Button bUbicacion = view.findViewById(R.id.btnUbicacion);
        Button bCrearReporte = view.findViewById(R.id.btnCrearReporte);

        // INICIALIZA NUEVO REPORTE
        nuevo = new Reporte();
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
                // Toma las coordenadas desde el ViewModel compartido con el UbicacionFragment
                double latitude = sharedLocationViewModel.getLatitude();
                double longitude = sharedLocationViewModel.getLongitude();

                // Crea el objeto Reporte y carga las coordenadas
                nuevo = new Reporte();
                nuevo.setLatitud(latitude);
                nuevo.setLongitud(longitude);

                nuevo.setImagen(imagenCapturada);

                // Referencia al ImageView - prueba de imagen
                ImageView imgViewFotoTomada = getView().findViewById(R.id.imgViewFotoTomada);

                // Modificación de tamaño - prueba de imagen
                Bitmap imagenRedimensionada = Bitmap.createScaledBitmap(imagenCapturada, imagenCapturada.getWidth()*3, imagenCapturada.getHeight()*3, true);

                // Configura la imagen capturada en el ImageView - prueba de imagen
                imgViewFotoTomada.setImageBitmap(imagenRedimensionada);

                Log.i("Ubicacion Prueba", "Coordenadas: " + nuevo.getLatitud() + " - " + nuevo.getLongitud());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // VALIDA QUE LA ACCION SE HAYA COMPLETADO CORRECTAMENTE
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            // La imagen se capturó exitosamente
            Bundle extras = data.getExtras();
            imagenCapturada = (Bitmap) extras.get("data");
        }
    }

    private void navigateToLocationFragment() {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.elegir_ubicacion);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NuevoReporteViewModel.class);
        // TODO: Use the ViewModel
    }

}