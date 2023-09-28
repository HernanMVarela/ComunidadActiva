package frgp.utn.edu.ar.controllers.ui.buscar_reporte;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BuscarReporteViewModel.class);
        // TODO: Use the ViewModel
    }

}