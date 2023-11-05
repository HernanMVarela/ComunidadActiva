package frgp.utn.edu.ar.controllers.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAListviewProyectos;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMASpinnerEstadosProyectos;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMASpinnerTiposProyectos;
import frgp.utn.edu.ar.controllers.ui.viewmodels.BuscarProyectoViewModel;

public class BuscarProyectoFragment extends Fragment {

    private BuscarProyectoViewModel mViewModel;
    private ListView listaDeProyectos;
    private SearchView barraBusqueda;
    private Proyecto proyectoSeleccionado=null;
    private View viewSeleccionada=null;
    private Spinner spEstadoP, spTipoProyecto;
    private int buscadorE=1, buscadorT=1, usarE=0, usarT=0;
    private SwitchCompat swTipo, swEstado;
    private Button verDetalle;

    public static BuscarProyectoFragment newInstance() {
        return new BuscarProyectoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buscar_proyecto, container, false);
        verDetalle = view.findViewById(R.id.btnVerDetalleP);
        spTipoProyecto = view.findViewById(R.id.spFiltroA);
        spEstadoP = view.findViewById(R.id.spFiltroB);
        barraBusqueda = view.findViewById(R.id.busquedaProyecto);
        swEstado = view.findViewById(R.id.swEstado);
        swTipo = view.findViewById(R.id.swTipo);
        DMASpinnerTiposProyectos tiposPro = new DMASpinnerTiposProyectos(spTipoProyecto, getContext());
        tiposPro.execute();
        DMASpinnerEstadosProyectos estadosP = new DMASpinnerEstadosProyectos(spEstadoP, getContext());
        estadosP.execute();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        listaDeProyectos = view.findViewById(R.id.listProyectos);
        barraBusqueda.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cargarProyectos(view);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    cargarProyectos(view);
                }
                if (newText.length() > 25) {
                    barraBusqueda.setQuery(newText.substring(0, 25), false);
                }
                return false;
            }
        });
        swTipo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    usarT=1;
                } else {
                    usarT=0;
                }
                cargarProyectos(view);
            }
        });
        swEstado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    usarE=1;
                } else {
                    usarE=0;
                }
                cargarProyectos(view);
            }
        });
        spTipoProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cargarProyectos(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        spEstadoP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cargarProyectos(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        listaDeProyectos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (viewSeleccionada != null) {
                    viewSeleccionada.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_500));
                }
                proyectoSeleccionado = (Proyecto) parent.getItemAtPosition(position);
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_700));
                viewSeleccionada = view;
            }
        });
        verDetalle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                iraDetalles();
            }
        });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BuscarProyectoViewModel.class);
        // TODO: Use the ViewModel
    }

    public void iraDetalles(){
        if(proyectoSeleccionado != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("proyectoactual", proyectoSeleccionado);
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.detalleProyectoFragment, bundle);
        }else {
            Toast.makeText(this.getContext(), "Debes seleccionar un proyecto.", Toast.LENGTH_LONG).show();
        }
    }
    public void cargarProyectos(View view){
        buscadorT = spTipoProyecto.getSelectedItemPosition()+1;
        if(buscadorT<1){buscadorT=1;}
        buscadorE = spEstadoP.getSelectedItemPosition()+1;
        if(buscadorE<1){buscadorE=1;}
        DMAListviewProyectos DMAListaP = new DMAListviewProyectos(listaDeProyectos,view.getContext(),usarT,usarE,buscadorT,buscadorE,barraBusqueda.getQuery().toString());
        DMAListaP.execute();
    }
}