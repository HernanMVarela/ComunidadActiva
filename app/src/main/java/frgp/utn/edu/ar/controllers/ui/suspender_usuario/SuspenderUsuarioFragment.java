package frgp.utn.edu.ar.controllers.ui.suspender_usuario;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frgp.utn.edu.ar.controllers.R;

public class SuspenderUsuarioFragment extends Fragment {

    private SuspenderUsuarioViewModel mViewModel;

    public static SuspenderUsuarioFragment newInstance() {
        return new SuspenderUsuarioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suspender_usuario, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SuspenderUsuarioViewModel.class);
        // TODO: Use the ViewModel
    }

}