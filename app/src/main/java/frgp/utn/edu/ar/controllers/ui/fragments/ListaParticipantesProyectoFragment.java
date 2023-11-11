package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;
import android.widget.Toast;

import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.model.Voluntario;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAAbandonarProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMABuscarUsuarioPorProyecto;
import frgp.utn.edu.ar.controllers.ui.activities.HomeActivity;
import frgp.utn.edu.ar.controllers.ui.dialogs.UserDetailDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.ListaParticipantesProyectoViewModel;
import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class ListaParticipantesProyectoFragment extends Fragment {
    private final SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private ListaParticipantesProyectoViewModel mViewModel;
    private static final String ARG_PROYECTO_ID = "proyecto_id";
    private ListView listado;
    private Proyecto seleccionado = null;
    private Voluntario voluntario = null;
    private View viewSeleccionado = null;
    private Usuario loggedInUser = null;
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
        loggedInUser = sharedPreferences.getUsuarioData(getContext());
        if(loggedInUser == null){
            Intent registro = new Intent(getContext(), HomeActivity.class);
            startActivity(registro);
        }
        Bundle bundle = this.getArguments();
        /// OBTIENE EL REPORTE SELECCIONADO EN LA PANTALLA ANTERIOR
        if (bundle != null) {
            seleccionado = (Proyecto) bundle.getSerializable("proyectoactual");
            /// VALIDA QUE EL REPORTE EXISTA
            if (seleccionado != null) {
                listado = view.findViewById(R.id.lv_listado_participantes);
                TextView titulo = view.findViewById(R.id.txt_lista_participantes);
                titulo.setText(seleccionado.getTitulo());
                comportamiento_listado(listado);
                Button bDetalleUser = view.findViewById(R.id.btn_listado_participantes_detalle);
                comportamiento_boton_usuario(bDetalleUser);
                Button bEliminarSelected = view.findViewById(R.id.btn_listado_participantes_eliminar);
                comportamiento_boton_eliminar(bEliminarSelected);
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
            cargar_lista_participantes();
        }
    }
    private void cargar_lista_participantes(){
        /// CARGA LISTA DE VOLUNTARIOS DEL PROYECTO
        DMABuscarUsuarioPorProyecto DMAListaParticipantes = new DMABuscarUsuarioPorProyecto(listado,getContext(),seleccionado.getId());
        DMAListaParticipantes.execute();
    }

    private void comportamiento_boton_usuario(Button bUsuario){
        bUsuario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // BOTON DETALLE DE VOLUNTARIO
                if(voluntario!=null){
                    Usuario user = new Usuario();
                    user.setId(voluntario.getId());
                    user.setUsername(voluntario.getUsername());
                    user.setFecha_alta(voluntario.getFecha_alta());
                    user.setFecha_nac(voluntario.getFecha_nac());
                    user.setPuntuacion(voluntario.getPuntuacion());
                    user.setTelefono(voluntario.getTelefono());
                    user.setTipo(voluntario.getTipo());
                    user.setNombre(voluntario.getNombre());
                    user.setApellido(voluntario.getApellido());
                    user.setCorreo(voluntario.getCorreo());
                    user.setEstado(voluntario.getEstado());
                    UserDetailDialogFragment dialogFragment = UserDetailDialogFragment.newInstance(user);
                    dialogFragment.show(getFragmentManager(), "user_detail");
                }else{
                    Toast.makeText(getContext(), "Debes seleccionar un usuario", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void comportamiento_listado(ListView listado){
        listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Restaura el fondo del elemento previamente seleccionado
                if (viewSeleccionado != null) {
                    viewSeleccionado.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_500));
                }
                // Almacena el informe seleccionado en una variable
                voluntario = (Voluntario) parent.getItemAtPosition(position);
                // Cambia el fondo del elemento seleccionado
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_700));
                // Almacena la vista del elemento seleccionado actualmente
                viewSeleccionado = view;
            }
        });
    }

    private void comportamiento_boton_eliminar(Button bEliminar){
        bEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(voluntario.getId()==loggedInUser.getId()){
                    Toast.makeText(getContext(),"No puedes eliminarte del tu proyecto",Toast.LENGTH_LONG).show();
                }else{
                    /// TERMINAR FUNCIONALIDAD
                    try {
                        if (voluntario != null) {
                            DMAAbandonarProyecto DMAAbandonar = new DMAAbandonarProyecto(voluntario.getId(),seleccionado.getId());
                            DMAAbandonar.execute();
                            if(DMAAbandonar.get()){
                                Toast.makeText(getContext(),voluntario.getUsername() + " eliminado!",Toast.LENGTH_LONG).show();
                                cargar_lista_participantes();
                            }else{
                                Toast.makeText(getContext(),"Error al eliminar",Toast.LENGTH_LONG).show();
                            }
                        }{
                            Toast.makeText(getContext(),"Ningun usuario seleccionado",Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListaParticipantesProyectoViewModel.class);
        // TODO: Use the ViewModel
    }

}