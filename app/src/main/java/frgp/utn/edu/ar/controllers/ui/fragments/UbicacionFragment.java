package frgp.utn.edu.ar.controllers.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.ui.viewmodels.SharedLocationViewModel;

public class UbicacionFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private SharedLocationViewModel sharedLocationViewModel;
    private GoogleMap googlemaplocal;
    private Marker userMarker;
    private Button botonGuardar;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            // Verifica si tiene permisos de ubicación
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Si no tiene permisos, se solicitan al usuario
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                // Si ya tiene permisos, accede a la ubicación
                FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
                locationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                            List<Address> addresses;

                            try {
                                googlemaplocal = googleMap;
                                addresses = geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1);
                                if (!addresses.isEmpty()) {
                                    userMarker = googlemaplocal.addMarker(new MarkerOptions().position(currentLatLng).title(addresses.get(0).getAddressLine(0)));
                                    googlemaplocal.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                                } else {
                                    userMarker = googlemaplocal.addMarker(new MarkerOptions().position(currentLatLng).title("Sin título"));
                                    googlemaplocal.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                                }
                            } catch (IOException e) {
                                Log.e("Error de mapa", e.toString());
                            }
                        }
                    }
                });

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        if (userMarker != null) {
                            userMarker.remove();
                        }
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> addresses;
                        try {
                            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            if (!addresses.isEmpty()) {
                                userMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title(addresses.get(0).getAddressLine(0)));
                                // Mueve la cámara a la ubicación del clic
                                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            }else {
                                userMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title("Sin título"));
                                // Mueve la cámara a la ubicación del clic
                                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ubicacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        sharedLocationViewModel = new ViewModelProvider(requireActivity()).get(SharedLocationViewModel.class);

        botonGuardar = view.findViewById(R.id.botonGuardar);
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verifica si se ha seleccionado una ubicación
                if (userMarker != null) {
                    LatLng selectedLocation = userMarker.getPosition();
                    double latitude = selectedLocation.latitude;
                    double longitude = selectedLocation.longitude;

                    sharedLocationViewModel.setLatitude(latitude);
                    sharedLocationViewModel.setLongitude(longitude);

                    getParentFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(),"No se ha seleccionado una ubicación", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    // Manejo de la respuesta de solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido - Reinicia el mapa
                callback.onMapReady(googlemaplocal);
            } else {
                // Permiso denegado, muestra un mensaje al usuario
                Toast.makeText(requireContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
