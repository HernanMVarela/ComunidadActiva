package frgp.utn.edu.controllers.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.controllers.ui.viewmodels.NotificarCerrarDenunciaViewModel;

public class NotificarCerrarDenunciaFragment extends Fragment {

    private NotificarCerrarDenunciaViewModel mViewModel;

    public static NotificarCerrarDenunciaFragment newInstance() {
        return new NotificarCerrarDenunciaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notificar_cerrar_denuncia, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificarCerrarDenunciaViewModel.class);
        // TODO: Use the ViewModel
    }

}