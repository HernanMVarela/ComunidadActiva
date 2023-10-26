package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMAListviewReportes;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMAListviewReportesPorTexto;
import frgp.utn.edu.ar.controllers.ui.viewmodels.BuscarReporteViewModel;

public class BuscarReporteFragment extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private BuscarReporteViewModel mViewModel;
    private ListView listaReportes;
    private GoogleMap googlemaplocal;
    private SearchView barraBusqueda;
    private LatLng ubicacionMapa;
    private String textoBusqueda = "";
    private Reporte seleccionado = null;
    private View viewSeleccionado = null;
    public static BuscarReporteFragment newInstance() {
        return new BuscarReporteFragment();
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        public void onMapReady(GoogleMap googleMap) {
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
                        if (location != null) { // VERIFICA QUE EXISTA UBICACION
                            /// OBTIENE LA UBICACIÓN ACTUAL DEL DISPOSITIVO
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            /// INICIALIZA EL GEOCODER PARA OBTENER DATOS DE LAS COORDENADAS
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                            List<Address> addresses;
                            googlemaplocal = googleMap;
                            /// VALIDA SI EXISTE UN FILTO DE BUSQUEDA ACTIVO
                            if (!textoBusqueda.isEmpty()) {
                                googlemaplocal.clear(); /// LIMPIA LOS MARCADORES DEL MAPA
                                DMAListviewReportesPorTexto DMAListaReportes = new DMAListviewReportesPorTexto(listaReportes, getContext(), currentLatLng, googlemaplocal, textoBusqueda);
                                DMAListaReportes.execute();
                            } else {
                                DMAListviewReportes DMAListaReportes = new DMAListviewReportes(listaReportes, getContext(), currentLatLng, googlemaplocal);
                                DMAListaReportes.execute();
                            }
                            try {
                                addresses = geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1);
                                if (!addresses.isEmpty()) {
                                    googlemaplocal.addMarker(new MarkerOptions().position(currentLatLng).title(addresses.get(0).getAddressLine(0)));
                                    googlemaplocal.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12));
                                } else {
                                    googlemaplocal.addMarker(new MarkerOptions().position(currentLatLng).title("Sin título"));
                                    googlemaplocal.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12));
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
                        // Borra el reporte seleccionado al hacer clic en cualquier otra parte del mapa
                        seleccionado = null;
                    }
                });
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        // Accede a las coordenadas del marcador en el mapa
                        ubicacionMapa = marker.getPosition();
                        // BUSCAR EL REPORTE QUE COINCIDA CON LAS COORDENADAS SELECCIONADAS
                        for(int i = 0; i < listaReportes.getCount(); i++){
                            Reporte reporte = (Reporte) listaReportes.getItemAtPosition(i);
                            if (reporte.getLatitud() == ubicacionMapa.latitude && reporte.getLongitud() == ubicacionMapa.longitude) {
                                seleccionado = (Reporte) listaReportes.getItemAtPosition(i);
                                break;
                            }
                        }
                        return false;
                    }
                });
            }
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buscar_reporte, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // BOTON VER REPORTE - PRUEBA
        Button bVerReporte = view.findViewById(R.id.btnVerDetalle);

        listaReportes = view.findViewById(R.id.listReportes);
        barraBusqueda = view.findViewById(R.id.busquedaReporte);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapListaReportes);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }else{
            DMAListviewReportes DMAListaReportes = new DMAListviewReportes(listaReportes,view.getContext(),new LatLng(0,0), googlemaplocal);
            DMAListaReportes.execute();
        }

        barraBusqueda.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textoBusqueda = query.trim();
                if (mapFragment != null) {
                    mapFragment.getMapAsync(callback);
                }else{
                    DMAListviewReportesPorTexto DMAListaReportes = new DMAListviewReportesPorTexto(listaReportes,getContext(),new LatLng(0,0), googlemaplocal,textoBusqueda);
                    DMAListaReportes.execute();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // Si el texto está vacío, restaura los parámetros de búsqueda o realiza alguna otra acción
                    textoBusqueda = "";
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(callback);
                    }else{
                        DMAListviewReportesPorTexto DMAListaReportes = new DMAListviewReportesPorTexto(listaReportes,getContext(),new LatLng(0,0), googlemaplocal,textoBusqueda);
                        DMAListaReportes.execute();
                    }
                }
                return false;
            }
        });

        listaReportes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Restaura el fondo del elemento previamente seleccionado
                if (viewSeleccionado != null) {
                    viewSeleccionado.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_500));
                }
                // Almacena el informe seleccionado en una variable
                seleccionado = (Reporte) parent.getItemAtPosition(position);
                // Cambia el fondo del elemento seleccionado
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_700));
                // Almacena la vista del elemento seleccionado actualmente
                viewSeleccionado = view;
            }
        });
        bVerReporte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                navegarDetalle();
            }
        });
    }

    public void navegarDetalle(){
        if(seleccionado != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("selected_report", seleccionado);
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.detalle_reporte, bundle);
        }else {
            Toast.makeText(this.getContext(), "Debes seleccionar un reporte", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BuscarReporteViewModel.class);
        // TODO: Use the ViewModel
    }

}