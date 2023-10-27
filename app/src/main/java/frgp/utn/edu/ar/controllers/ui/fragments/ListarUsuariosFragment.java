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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMAListarUsuariosCompleto;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMAListarUsuariosConFiltros;
import frgp.utn.edu.ar.controllers.ui.viewmodels.ListarUsuariosViewModel;

public class ListarUsuariosFragment extends Fragment {

    private ListarUsuariosViewModel mViewModel;
    ListView listado;
    SearchView searchView;
    Spinner tipoUser;

    public static ListarUsuariosFragment newInstance() {
        return new ListarUsuariosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listar_usuarios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] opciones = {"VECINO", "MODERADOR", "ADMINISTRADOR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, opciones);

        listado = view.findViewById(R.id.lstUsuarios);
        searchView = view.findViewById(R.id.svBusquedaUser);
        tipoUser = view.findViewById(R.id.spTipoUser);

        tipoUser.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                DMAListarUsuariosConFiltros DMAListadoFiltrado = new DMAListarUsuariosConFiltros(getContext(), listado, tipoUser.getSelectedItem().toString(), searchView.getQuery().toString());
                DMAListadoFiltrado.execute();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    DMAListarUsuariosCompleto DMAListado = new DMAListarUsuariosCompleto(getContext(), listado);
                    DMAListado.execute();
                }
                return false;
            }
        });
        tipoUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                DMAListarUsuariosConFiltros DMAListadoFiltrado = new DMAListarUsuariosConFiltros(getContext(), listado, tipoUser.getSelectedItem().toString(), searchView.getQuery().toString());
                DMAListadoFiltrado.execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListarUsuariosViewModel.class);
        // TODO: Use the ViewModel
    }
}