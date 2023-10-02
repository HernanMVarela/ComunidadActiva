package frgp.utn.edu.ar.controllers.ui.nuevo_reporte;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import frgp.utn.edu.ar.controllers.R;

public class NuevoReporteFragment extends Fragment {
    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int LOCATION_PERMISSION_REQUEST = 123;
    private NuevoReporteViewModel mViewModel;

    public static NuevoReporteFragment newInstance() {
        return new NuevoReporteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nuevo_reporte, container, false);

        Button bCamara = view.findViewById(R.id.btnCamara);
        Button bUbicacion = view.findViewById(R.id.btnUbicacion);
        Button bCrearReporte = view.findViewById(R.id.btnCrearReporte);
        bCamara.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Verificar si tenemos el permiso de la cámara
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                } else {
                    // Si no tenemos el permiso, solicítalo al usuario
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PIC_REQUEST);
                }

            }
        });
        bUbicacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Verificar si tenemos el permiso de la cámara
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                    navController.navigate(R.id.elegir_ubicacion);
                } else {
                    // Si no tenemos el permiso, solicítalo al usuario
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);                }

            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NuevoReporteViewModel.class);
        // TODO: Use the ViewModel
    }

}