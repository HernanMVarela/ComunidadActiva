package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.core.content.ContextCompat;
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
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.widget.Spinner;
import android.widget.Toast;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMAListarUsuariosCompleto;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMAListarUsuariosConFiltros;
import frgp.utn.edu.ar.controllers.ui.viewmodels.ListarUsuariosViewModel;

public class ListarUsuariosFragment extends Fragment {

    private ListarUsuariosViewModel mViewModel;
    private ListView listado;
    private SearchView searchView;
    private Spinner tipoUser;
    private Usuario selectedUser = null;
    private View viewSeleccionado = null;

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

        /// COMPORTAMIENTO DE LA BARRA DE BUSQUEDA
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
                    DMAListarUsuariosConFiltros DMAListadoFiltrado = new DMAListarUsuariosConFiltros(getContext(), listado, tipoUser.getSelectedItem().toString(), "");
                    DMAListadoFiltrado.execute();
                }
                if (newText.length() > 20) {
                    searchView.setQuery(newText.substring(0, 20), false);
                }
                return false;
            }
        });

        /// COMPORTAMIENTO DEL SPINNER - SELECCIÓN DE ITEM
        tipoUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                DMAListarUsuariosConFiltros DMAListadoFiltrado = new DMAListarUsuariosConFiltros(getContext(), listado, tipoUser.getSelectedItem().toString(), searchView.getQuery().toString());
                DMAListadoFiltrado.execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        /// COMPORTAMIENTO DEL LISTADO - SELECCIÓN DE USUARIO
        listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Restaura el fondo del elemento previamente seleccionado
                if (viewSeleccionado != null) {
                    viewSeleccionado.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_500));
                }
                // Almacena el informe seleccionado en una variable
                selectedUser = (Usuario)listado.getItemAtPosition(i);
                // Cambia el fondo del elemento seleccionado
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_700));
                // Almacena la vista del elemento seleccionado actualmente
                viewSeleccionado = view;
                navegarDetalle();
            }
        });
    }

    public void navegarDetalle(){
        if(selectedUser != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("selected_user", selectedUser);
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_listar_usuario_to_nav_ver_usuario, bundle);
        }else {
            Toast.makeText(this.getContext(), "Debes seleccionar un usuario", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListarUsuariosViewModel.class);
        // TODO: Use the ViewModel
    }
}