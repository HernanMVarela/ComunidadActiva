package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.DenunciaNuevo;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMAListarDenunciaProyecto;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMAListarDenunciasReporte;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMASpinnerTiposDenuncia;
import frgp.utn.edu.ar.controllers.ui.viewmodels.ListarDenunciaViewModel;

public class ListarDenunciaFragment extends Fragment {

    private ListarDenunciaViewModel mViewModel;
    private ListView listaDenuncias;
    private DenunciaNuevo seleccionado = null;
    private View viewSeleccionado = null;
    List<Denuncia> denunciaList = new ArrayList<>();
    private Spinner spinTipoDenuncia;

    public static ListarDenunciaFragment newInstance() {
        return new ListarDenunciaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listar_denuncia, container, false);

        spinTipoDenuncia = view.findViewById(R.id.spnTipoDenuncia);

        DMASpinnerTiposDenuncia dataActivityTiposDenuncia = new DMASpinnerTiposDenuncia(spinTipoDenuncia, getContext());
        dataActivityTiposDenuncia.execute();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listaDenuncias = view.findViewById(R.id.listDenuncias);

        spinTipoDenuncia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(spinTipoDenuncia.getSelectedItem().toString().equals("1 - REPORTE")){
                    Toast.makeText(getContext(), "ENTRE A REPORTE", Toast.LENGTH_LONG).show();
                    DMAListarDenunciasReporte DMAListaDenuncias = new DMAListarDenunciasReporte(listaDenuncias,view.getContext());
                    DMAListaDenuncias.execute();
                }
                if(spinTipoDenuncia.getSelectedItem().toString().equals("2 - PROYECTO")){
                    Toast.makeText(getContext(), "ENTRE A PROYECTO", Toast.LENGTH_LONG).show();
                    DMAListarDenunciaProyecto DMAListaDenuncias = new DMAListarDenunciaProyecto(listaDenuncias,view.getContext());
                    DMAListaDenuncias.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //esto va luego de que selecciona opcion del spinner
        //DMAListarDenunciasReporte DMAListaDenuncias = new DMAListarDenunciasReporte(listaDenuncias,view.getContext());
       //DMAListaDenuncias.execute();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListarDenunciaViewModel.class);
        // TODO: Use the ViewModel
    }

}