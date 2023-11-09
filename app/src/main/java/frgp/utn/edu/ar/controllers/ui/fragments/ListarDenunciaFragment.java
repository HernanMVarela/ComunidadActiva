package frgp.utn.edu.ar.controllers.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMASpinnerTiposDenuncia;
import frgp.utn.edu.ar.controllers.data.repository.denuncia.DenunciaRepository;
import frgp.utn.edu.ar.controllers.ui.adapters.ListarDenunciaAdapter;

public class ListarDenunciaFragment extends Fragment {

    private List<Denuncia> denuncias;
    private ListView listaDenuncias;
    private Denuncia seleccionado = null;
    private View viewSeleccionado = null;
    private List<Denuncia> denunciaList = new ArrayList<>();
    private Spinner spinTipoDenuncia;
    private DenunciaRepository denunciaRepository = new DenunciaRepository();

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
                    denuncias = denunciaRepository.listarDenunciasReportes();
                }
                if(spinTipoDenuncia.getSelectedItem().toString().equals("2 - PROYECTO")){
                    denuncias = denunciaRepository.listarDenunciasProyectos();

                }

                if(denuncias!=null) {
                    ListarDenunciaAdapter adapter = new ListarDenunciaAdapter(view.getContext(), denuncias);
                    listaDenuncias.setAdapter(adapter);
                }
                else {
                    Toast.makeText(view.getContext(), "No hay denuncias para mostrar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        listaDenuncias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Almacena el informe seleccionado en una variable
                seleccionado = (Denuncia) adapterView.getItemAtPosition(i);
                // Almacena la vista del elemento seleccionado actualmente
                viewSeleccionado = view;
                navegarDetalle();
            }
        });
    }

    public void navegarDetalle(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected_denuncia", seleccionado);
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.nav_ver_denuncia, bundle);
    }
}