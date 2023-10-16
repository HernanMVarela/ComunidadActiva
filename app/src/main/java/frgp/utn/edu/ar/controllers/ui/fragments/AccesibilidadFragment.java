package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.ui.viewmodels.AccesibilidadViewModel;

public class AccesibilidadFragment extends Fragment {

    private AccesibilidadViewModel mViewModel;

    public static AccesibilidadFragment newInstance() {
        return new AccesibilidadFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accesibilidad, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AccesibilidadViewModel.class);
        // TODO: Use the ViewModel
    }

}