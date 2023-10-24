package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.DenunciaNuevo;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMAListarDenunciasReporte;
import frgp.utn.edu.ar.controllers.ui.viewmodels.ListarDenunciaViewModel;

public class ListarDenunciaFragment extends Fragment {

    private ListarDenunciaViewModel mViewModel;
    private ListView listaDenuncias;
    private DenunciaNuevo seleccionado = null;
    private View viewSeleccionado = null;
    List<Denuncia> denunciaList = new ArrayList<>();


    public static ListarDenunciaFragment newInstance() {
        return new ListarDenunciaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //denunciaList.add(new Denuncia());
        //denunciaList.add(new Denuncia());


        return inflater.inflate(R.layout.fragment_listar_denuncia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listaDenuncias = view.findViewById(R.id.listDenuncias);

        DMAListarDenunciasReporte DMAListaDenuncias = new DMAListarDenunciasReporte(listaDenuncias,view.getContext());
        DMAListaDenuncias.execute();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListarDenunciaViewModel.class);
        // TODO: Use the ViewModel
    }

}