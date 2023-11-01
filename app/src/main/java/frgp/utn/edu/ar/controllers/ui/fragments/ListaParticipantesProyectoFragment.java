package frgp.utn.edu.ar.controllers.ui.fragments;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMABuscarUsuarioPorProyecto;
import frgp.utn.edu.ar.controllers.ui.viewmodels.ListaParticipantesProyectoViewModel;
import frgp.utn.edu.ar.controllers.R;

public class ListaParticipantesProyectoFragment extends Fragment {

    private ListaParticipantesProyectoViewModel mViewModel;
    private static final String ARG_PROYECTO_ID = "proyecto_id";
    private ListView listado;
    private Proyecto seleccionado;
    public static ListaParticipantesProyectoFragment newInstance(int proyectoId) {
        ListaParticipantesProyectoFragment fragment = new ListaParticipantesProyectoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROYECTO_ID, proyectoId);
        fragment.setArguments(args);
        return fragment;
    }

    public static ListaParticipantesProyectoFragment newInstance() {
        return new ListaParticipantesProyectoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_participantes_proyecto, container, false);
        Bundle bundle = this.getArguments();
        /// OBTIENE EL REPORTE SELECCIONADO EN LA PANTALLA ANTERIOR
        if (bundle != null) {
            seleccionado = (Proyecto) bundle.getSerializable("proyectoactual");
            /// VALIDA QUE EL REPORTE EXISTA
            if (seleccionado != null) {
                listado = view.findViewById(R.id.lv_listado_participantes);
                TextView titulo = view.findViewById(R.id.txt_lista_participantes);
                titulo.setText(seleccionado.getTitulo());
            }else {
                /// MODIFICAR PARA REGRESAR A PANTALLA ANTERIOR
                Toast.makeText(this.getContext(), "ERROR AL CARGAR", Toast.LENGTH_LONG).show();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.popBackStack();
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(seleccionado!=null){
            DMABuscarUsuarioPorProyecto DMAListaParticipantes = new DMABuscarUsuarioPorProyecto(listado,getContext(),seleccionado.getId());
            DMAListaParticipantes.execute();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListaParticipantesProyectoViewModel.class);
        // TODO: Use the ViewModel
    }

}