package frgp.utn.edu.ar.controllers.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAAbandonarProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMABuscarUsuarioEnProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMASpinnerEstadosProyectos;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMASpinnerEstadosProyectosSinDenuncia;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAUnirseAProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAUpdateProyecto;
import frgp.utn.edu.ar.controllers.ui.activities.HomeActivity;
import frgp.utn.edu.ar.controllers.ui.dialogs.UserDetailDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.DetalleReporteViewModel;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class DetalleProyectoFragment extends Fragment {

    SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private DetalleReporteViewModel mViewModel;
    private GoogleMap googlemaplocal;
    private TextView titulo, descripcion, estado, tipo, requerimiento, contacto, cupo;
    private Button btnUnirseP;
    private boolean control=false;
    Usuario loggedInUser = null;
    private Proyecto seleccionado;
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
        btnUnirseP = view.findViewById(R.id.btnUnirseDP);

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
        btnUnirseP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loggedInUser.getId()==seleccionado.getOwner().getId() && seleccionado.getEstado().getEstado().equals("ABIERTO")){
                    DMAUpdateProyecto updatear = new DMAUpdateProyecto(seleccionado.getId(),1, getContext());
                    updatear.execute();
                }
                else{
                    controladorDeSituacion();
                        if(control)
                        {
                        DMAUnirseAProyecto unirse = new DMAUnirseAProyecto(loggedInUser.getId(), seleccionado.getId(), btnUnirseP, getContext());
                        unirse.execute();
                        }
                        else {
                        DMAAbandonarProyecto abandonar = new DMAAbandonarProyecto(loggedInUser.getId(), seleccionado.getId(), btnUnirseP, getContext());
                        abandonar.execute();
                    }
                }
            }
        });
        Button bUsuario = view.findViewById(R.id.btn_detalle_proyecto_owner);
        comportamiento_boton_usuario(bUsuario);

    }

    private void cargarDatos(){
        titulo.setText(seleccionado.getTitulo());
        descripcion.setText(seleccionado.getDescripcion());
        estado.setText(seleccionado.getEstado().getEstado());
        tipo.setText(seleccionado.getTipo().getTipo());
        requerimiento.setText(seleccionado.getRequerimientos());
        contacto.setText(seleccionado.getContacto());
        cupo.setText(String.valueOf(seleccionado.getCupo()));
        int idEstado = seleccionado.getEstado().getId();
        if(loggedInUser.getId()==seleccionado.getOwner().getId()&&idEstado!=5){
            /*
            idEstado-=1;
            spEstadoDP.setSelection(idEstado);
            btnUnirseP.setText("Actualizar");
            spEstadoDP.setVisibility(View.VISIBLE);
            estado.setVisibility(View.GONE);
            */
        }
        else{
            //spEstadoDP.setVisibility(View.GONE);
            //estado.setVisibility(View.VISIBLE);
            controladorDeSituacion();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetalleReporteViewModel.class);
        // TODO: Use the ViewModel
    }
    public void controladorDeSituacion(){
        /*
        spEstadoDP.setVisibility(View.GONE);
        estado.setVisibility(View.VISIBLE);
        DMABuscarUsuarioEnProyecto buscar = new DMABuscarUsuarioEnProyecto(loggedInUser.getId(),seleccionado.getId(),btnUnirseP,getContext());
        buscar.execute();
        if (btnUnirseP.getText().toString().equals("Unirse")) {
            control = true;
        } else {
            control = false;
        }
         */
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

}