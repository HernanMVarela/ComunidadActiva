package frgp.utn.edu.ar.controllers.ui.ubicacion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class UbicacionFragment extends Fragment {

    LatLng frgputn;
    GoogleMap miMapa;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            frgputn = new LatLng(-34.45516856682106, -58.62416024823926);
            miMapa = googleMap;
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> datos;

            try {
                datos = geocoder.getFromLocation(frgputn.latitude, frgputn.longitude, 1);
                miMapa.addMarker(new MarkerOptions().position(frgputn).title(datos.get(0).getAddressLine(0)));
                miMapa.moveCamera(CameraUpdateFactory.newLatLngZoom(frgputn,15));

            } catch (IOException e) {
                throw new RuntimeException(e);
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
    }

}