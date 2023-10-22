package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAListviewProyectos;
import frgp.utn.edu.ar.controllers.ui.viewmodels.BuscarProyectoViewModel;

public class BuscarProyectoFragment extends Fragment {

    private BuscarProyectoViewModel mViewModel;
    private ListView listaDeProyectos;
    private SearchView buscarProyecto;
    private Proyecto proyectoSeleccionado=null;
    private View viewSeleccionada=null;
    private Button verDetalle;

    public static BuscarProyectoFragment newInstance() {
        return new BuscarProyectoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buscar_proyecto, container, false);
        verDetalle = view.findViewById(R.id.btnVerDetalleP);
        listaDeProyectos = view.findViewById(R.id.listProyectos);
        //buscarProyecto = view.findViewById(R.id.swBuscar);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        try{
            DMAListviewProyectos DMAListaP = new DMAListviewProyectos(listaDeProyectos,view.getContext());
            DMAListaP.execute();
        }
        catch (Error e){}

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
                //iraDetalles();
            }
        });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BuscarProyectoViewModel.class);
        // TODO: Use the ViewModel
    }

    /*public void iraDetalles(){
        if(proyectoSeleccionado != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("selected_proyect", proyectoSeleccionado);
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.detalle_, bundle);
        }else {
            Toast.makeText(this.getContext(), "Debes seleccionar un reporte", Toast.LENGTH_LONG).show();
        }
    }*/
}