package frgp.utn.edu.ar.controllers.ui.buscar_reporte;

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

public class BuscarReporteFragment extends Fragment {

    private BuscarReporteViewModel mViewModel;

    public static BuscarReporteFragment newInstance() {
        return new BuscarReporteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buscar_reporte, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button bVerReporte = view.findViewById(R.id.btnVerDetalle);
        bVerReporte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // BOTON VER REPORTE
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.detalle_reporte);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BuscarReporteViewModel.class);
        // TODO: Use the ViewModel
    }

}