package frgp.utn.edu.ar.controllers.ui.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompatSideChannelService;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.model.Voluntario;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAAbandonarProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMABuscarUsuarioEnProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMACargarVoluntario;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAReUnirseProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMASpinnerEstadosProyectos;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMASpinnerEstadosProyectosSinDenuncia;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAUnirseAProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAUpdateProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAUsuarioExisteEnProyecto;
import frgp.utn.edu.ar.controllers.ui.activities.HomeActivity;
import frgp.utn.edu.ar.controllers.ui.dialogs.DenunciaProyectoDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.DenunciaReporteDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.UserDetailDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.DetalleReporteViewModel;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class DetalleProyectoFragment extends Fragment {

    private SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private DetalleReporteViewModel mViewModel;
    private GoogleMap googlemaplocal;
    private TextView titulo, descripcion, estado, tipo, requerimiento, contacto, cupo;
    private Usuario loggedInUser = null;
    private Proyecto seleccionado;
    private Voluntario existe = null;
    public static DetalleProyectoFragment newInstance() {return new DetalleProyectoFragment();}

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        public void onMapReady(GoogleMap googleMap) {
            // Verifica si tiene permisos de ubicación
            if (googleMap != null) {
                googlemaplocal = googleMap;

                if (seleccionado != null) {
                    // Agrega un marcador en la ubicación del reporte por defecto
                    LatLng ubicacionReporte = new LatLng(seleccionado.getLatitud(), seleccionado.getLongitud());
                    googlemaplocal.addMarker(new MarkerOptions().position(ubicacionReporte).title(seleccionado.getTitulo()));
                    googlemaplocal.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionReporte, 15));
                }
            } else {
                Toast.makeText(getContext(), "ERROR AL CARGAR EL MAPA", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_proyectos, container, false);

        loggedInUser = sharedPreferences.getUsuarioData(getContext());
        if(loggedInUser == null){
            Intent registro = new Intent(getContext(), HomeActivity.class);
            startActivity(registro);
        }

        titulo = view.findViewById(R.id.txt_detalle_proyecto_titulo);
        descripcion = view.findViewById(R.id.txt_detalle_proyecto_descripcion);
        estado = view.findViewById(R.id.txt_detalle_proyecto_estado);
        tipo = view.findViewById(R.id.txt_detalle_proyecto_tipo);
        requerimiento = view.findViewById(R.id.txt_detalle_proyecto_requisitos);
        contacto = view.findViewById(R.id.txt_detalle_proyecto_contacto);
        cupo = view.findViewById(R.id.txt_detalle_proyecto_cupo);

        Bundle bundle = this.getArguments();
        /// OBTIENE EL REPORTE SELECCIONADO EN LA PANTALLA ANTERIOR
        if (bundle != null) {
            seleccionado = (Proyecto) bundle.getSerializable("proyectoactual");
            /// VALIDA QUE EL REPORTE EXISTA
            if (seleccionado != null) {
                cargarDatos();
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
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_proyecto);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        Button bUnirse = view.findViewById(R.id.btn_detalle_proyecto_unirse);
        Button bFinalizar = view.findViewById(R.id.btn_detalle_proyecto_finalizar);
        Button bUsuario = view.findViewById(R.id.btn_detalle_proyecto_owner);
        comportamiento_boton_usuario(bUsuario);
        Button bListado = view.findViewById(R.id.btn_detalle_proyecto_participantes);
        comportamiento_boton_participantes(bListado);
        Button bDenunciar = view.findViewById(R.id.btn_detalle_proyecto_denunciar);
        comportamiento_boton_denunciar(bDenunciar);

        if(loggedInUser.getId()==seleccionado.getOwner().getId()) {
            bUnirse.setVisibility(View.GONE);
            bDenunciar.setVisibility(View.GONE);
            if(seleccionado.getEstado().getEstado().equals("FINALIZADO")){
                bFinalizar.setVisibility(View.GONE);
            }
            else {
                bFinalizar.setVisibility(View.VISIBLE);
            }
            comportamiento_boton_finalizar(bFinalizar);
        }else
        {
            bFinalizar.setVisibility(View.GONE);
            bUnirse.setVisibility(View.VISIBLE);
            bDenunciar.setVisibility(View.VISIBLE);
            bUnirse.setEnabled(true);
            comportamiento_boton_unirse(bUnirse);
        }
    }

    private void cargarDatos(){
        titulo.setText(seleccionado.getTitulo());
        descripcion.setText(seleccionado.getDescripcion());
        estado.setText(seleccionado.getEstado().getEstado());
        tipo.setText(seleccionado.getTipo().getTipo());
        requerimiento.setText(seleccionado.getRequerimientos());
        contacto.setText(seleccionado.getContacto());
        cupo.setText(String.valueOf(seleccionado.getCupo()-1));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetalleReporteViewModel.class);
        // TODO: Use the ViewModel
    }

    /// COMPORTAMIENTO CONTROLES
    private void comportamiento_boton_usuario(Button bUsuario){
        bUsuario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // BOTON DETALLE DE USUARIO REPORTE
                UserDetailDialogFragment dialogFragment = UserDetailDialogFragment.newInstance(seleccionado.getOwner());
                dialogFragment.show(getFragmentManager(), "user_detail");
            }
        });
    }

    private void comportamiento_boton_unirse(Button bUnirse){
        bUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DMACargarVoluntario DMAExisteUsuario = new DMACargarVoluntario(loggedInUser.getId(),seleccionado.getId());
                    DMAExisteUsuario.execute();
                    existe = DMAExisteUsuario.get();
                    if(existe!=null){
                        if(existe.isActivo()){
                            bUnirse.setText("ABANDONAR PROYECTO");
                            comportamiento_abandonar(bUnirse);
                        }else{
                            comportamiento_reunirse(bUnirse);
                        }
                    }else{
                        comportamiento_unirse(bUnirse);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    private void comportamiento_boton_participantes(Button bParticipantes){
        bParticipantes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // BOTON LISTADO DE PARTICIPANTES
                Bundle bundle = new Bundle();
                bundle.putSerializable("proyectoactual", seleccionado);
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_nav_detalle_proyecto_to_lista_participantes, bundle);
            }
        });
    }

    private void comportamiento_abandonar(Button bAbandonar){
        try {
            DMAAbandonarProyecto DMAAbandonar = new DMAAbandonarProyecto(loggedInUser.getId(),seleccionado.getId());
            DMAAbandonar.execute();
            if(DMAAbandonar.get()){
                bAbandonar.setText("UNIRSE");
                existe.setActivo(false);
                Toast.makeText(getContext(),"Has abandonado el proyecto!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getContext(),"Ha ocurrido un error", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void comportamiento_unirse(Button bUnirse){
        if(seleccionado.getEstado().getEstado().equals("ABIERTO")){
            DMAUnirseAProyecto DMAUnirseProyecto = new DMAUnirseAProyecto(loggedInUser.getId(),seleccionado.getId());
            DMAUnirseProyecto.execute();
            try {
                if(DMAUnirseProyecto.get()){
                    bUnirse.setText("ABANDONAR PROYECTO");
                    existe.setActivo(true);
                    Toast.makeText(getContext(),"Voluntario agregado!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(),"No se pudo agregar al usuario!", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.e("Error", "No se pudo registrar la operación");
            }

        }else{
            Toast.makeText(getContext(),"El proyecto no está abierto", Toast.LENGTH_LONG).show();
        }

    }

    private void comportamiento_reunirse(Button bUnirse){
        if(seleccionado.getEstado().getEstado().equals("ABIERTO")){
            DMAReUnirseProyecto DMAREUnirse = new DMAReUnirseProyecto(loggedInUser.getId(),seleccionado.getId());
            DMAREUnirse.execute();
            try {
                if(DMAREUnirse.get()){
                    existe.setActivo(true);
                    bUnirse.setText("ABANDONAR PROYECTO");
                    Toast.makeText(getContext(),"Voluntario reingresado!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(),"No se pudo agregar al usuario!", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.e("Error", "No se pudo registrar la operación");
            }
        }else{
            Toast.makeText(getContext(),"El proyecto no está abierto", Toast.LENGTH_LONG).show();
        }
    }

    private void comportamiento_boton_finalizar(Button bFinalizar){
        bFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(seleccionado.getEstado().getEstado().equals("ABIERTO")){
                    try {
                        DMAUpdateProyecto Finalizar = new DMAUpdateProyecto(seleccionado.getId(),2, getContext());
                        Finalizar.execute();
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e("Error", "No se pudo realizar la operación");
                    }
                }else{
                    Toast.makeText(getContext(),"El proyecto no está abierto", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void comportamiento_boton_denunciar(Button bDenunciar){
        bDenunciar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(seleccionado.getEstado().getEstado().equals("DENUNCIADO")){
                    Toast.makeText(getContext(),"El proyecto ya se encuentra denunciado, gracias por tu interes.", Toast.LENGTH_LONG).show();
                }
                else {
                    Bundle args = new Bundle();
                    args.putInt("DidProyecto",seleccionado.getId());
                    args.putInt("DidUsuario",loggedInUser.getId());
                    DenunciaProyectoDialogFragment dialogFragment = new DenunciaProyectoDialogFragment();
                    dialogFragment.setArguments(args);
                    dialogFragment.show(getFragmentManager(), "layout_denuciar_proyecto");
                }

            }
        });
    }

}