package frgp.utn.edu.ar.controllers.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMABuscarReportePorId;
import frgp.utn.edu.ar.controllers.ui.dialogs.CerrarReporteDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.DenunciaReporteDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.ImagenReporteDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.UserDetailDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.ValorarReporteDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.DetalleReporteViewModel;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class DetalleReporteFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private DetalleReporteViewModel mViewModel;
    private TextView titulo, descripcion, estado, fecha, tipo;
    private RatingBar puntaje;
    private Usuario loggedInUser = null;
    private Reporte seleccionado;
    public static DetalleReporteFragment newInstance() {
        return new DetalleReporteFragment();
    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        public void onMapReady(GoogleMap googleMap) {
            // Verifica si tiene permisos de ubicación
            if (googleMap != null) {
                if (seleccionado != null) {
                    // Agrega un marcador en la ubicación del reporte por defecto
                    LatLng ubicacionReporte = new LatLng(seleccionado.getLatitud(), seleccionado.getLongitud());
                    googleMap.addMarker(new MarkerOptions().position(ubicacionReporte).title(seleccionado.getTitulo()));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionReporte, 15));
                }
            } else {
                Toast.makeText(getContext(), "ERROR AL CARGAR EL MAPA", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_reporte, container, false);

        titulo = view.findViewById(R.id.titulo_ver_reporte);
        descripcion = view.findViewById(R.id.descripcion_reporte);
        estado = view.findViewById(R.id.estado_ver_reporte);
        fecha = view.findViewById(R.id.reporte_detalle_fecha);
        tipo = view.findViewById(R.id.reporte_det_tipo);
        puntaje = view.findViewById(R.id.detalle_rep_rating);
        loggedInUser = sharedPreferences.getUsuarioData(getContext());

        Bundle bundle = this.getArguments();
        /// OBTIENE EL REPORTE SELECCIONADO EN LA PANTALLA ANTERIOR
        if (bundle != null) {
            seleccionado = (Reporte) bundle.getSerializable("selected_report");
            /// VALIDA QUE EL REPORTE EXISTA
            if (seleccionado != null) {
                cargarDatosReporte();
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
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_reporte);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        Button bUsuario = view.findViewById(R.id.btnUsernameDetalle);
        Button bSolicitarCierre = view.findViewById(R.id.btnCerrarReporte);
        Button bValorar = view.findViewById(R.id.btnValorarReporte);
        Button bDenunciar = view.findViewById(R.id.btnDenunciarReporte);
        Button bImagen = view.findViewById(R.id.btnImagenReporte);

        if (!seleccionado.getEstado().getEstado().equals("ABIERTO")) {
            bSolicitarCierre.setVisibility(View.GONE);
        }
        if(seleccionado.getEstado().getEstado().equals("DENUNCIADO")) {
            bDenunciar.setVisibility(View.GONE);
            bValorar.setVisibility(View.GONE);
        }
        if(seleccionado.getEstado().getEstado().equals("ELIMINADO")){
            Toast.makeText(getContext(), "Este reporte fue eliminado!", Toast.LENGTH_LONG).show();
            regresar();
        }
        if(seleccionado.getOwner().getUsername().equals(loggedInUser.getUsername())){
            bSolicitarCierre.setText("CERRAR REPORTE");
            bSolicitarCierre.setVisibility(View.VISIBLE);
            bValorar.setVisibility(View.GONE);
            bDenunciar.setVisibility(View.GONE);
            if(!seleccionado.getEstado().getEstado().equals("PENDIENTE")){
                bSolicitarCierre.setVisibility(View.GONE);
            }
        }

        String user_rep = "Detalle del usuario " + seleccionado.getOwner().getUsername();
        bUsuario.setText(user_rep);

        comportamiento_boton_usuario(bUsuario);
        comportamiento_boton_solicitar_cierre(bSolicitarCierre);
        comportamiento_boton_valorar(bValorar);
        comportamiento_boton_denunciar(bDenunciar);
        comportamiento_boton_imagen(bImagen);
    }


    private void actualizar_campos(){
        if(seleccionado!=null){
            try {
                DMABuscarReportePorId DMAReporte = new DMABuscarReportePorId(seleccionado.getId());
                DMAReporte.execute();
                seleccionado = DMAReporte.get();
                if(seleccionado!=null){
                    cargarDatosReporte();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            regresar();
        }
    }

    private void comportamiento_boton_usuario(Button bUsuario){
        bUsuario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // BOTON DETALLE DE USUARIO REPORTE
                UserDetailDialogFragment dialogFragment = UserDetailDialogFragment.newInstance(seleccionado.getOwner());
                dialogFragment.show(getFragmentManager(), "user_detail");
            }
        });
    }

    private void comportamiento_boton_imagen(Button bImagen){
        bImagen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // BOTON DETALLE DE USUARIO REPORTE
                ImagenReporteDialogFragment dialogFragment = ImagenReporteDialogFragment.newInstance(seleccionado.getId(), seleccionado.getTitulo());
                dialogFragment.show(getFragmentManager(), "reporte_imagen");
            }
        });
    }

    private void comportamiento_boton_solicitar_cierre(Button bSolicitarCierre){
        bSolicitarCierre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("selected_report", seleccionado);
                bundle.putSerializable("logged_in_user", loggedInUser);
                // BOTON SOLICITAR CIERRE
                if(seleccionado.getOwner().getUsername().equals(loggedInUser.getUsername())){
                    if(seleccionado.getEstado().getEstado().equals("PENDIENTE")){
                        /// SI EL USUARIO LOGUEADO ES EL DUEÑO DEL REPORTE
                        CerrarReporteDialogFragment dialogFragment = new CerrarReporteDialogFragment();
                        dialogFragment.setArguments(bundle); // Establece el Bundle como argumento
                        // Configura DetalleReporteFragment como el fragmento de destino
                        dialogFragment.show(getFragmentManager(), "layout_cerrar_reporte");
                        dialogFragment.setDismissListener(new CerrarReporteDialogFragment.DismissListener() {
                            @Override
                            public void onDismiss() {
                                actualizar_campos();
                            }
                        });
                    }else{
                        Toast.makeText(getContext(), "No puedes atender tu propio reporte", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (seleccionado.getEstado().getEstado().equals("ABIERTO")) {
                        /// CARGO
                        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                        navController.navigate(R.id.solicitar_cierre, bundle);
                    } else if (seleccionado.getEstado().getEstado().equals("PENDIENTE")) {
                        Toast.makeText(getContext(), "Este reporte está pendiente", Toast.LENGTH_LONG).show();
                    } else if (seleccionado.getEstado().getEstado().equals("CERRADO")) {
                        Toast.makeText(getContext(), "Este reporte ya se encuentra cerrado", Toast.LENGTH_LONG).show();
                    } else if (seleccionado.getEstado().getEstado().equals("CANCELADO")) {
                        Toast.makeText(getContext(), "Este reporte está cancelado, no se puede cerrar", Toast.LENGTH_LONG).show();
                    } else if (seleccionado.getEstado().getEstado().equals("DENUNCIADO")) {
                        Toast.makeText(getContext(), "El reporte está denunciado, no se puede cerrar", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void comportamiento_boton_valorar(Button bValorar){
        bValorar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // BOTON VALORAR REPORTE
                Bundle args = new Bundle();
                args.putSerializable("selected_report", seleccionado);
                args.putSerializable("logged_in_user", loggedInUser);
                ValorarReporteDialogFragment dialogFragment = new ValorarReporteDialogFragment();
                dialogFragment.setArguments(args); // Establece el Bundle como argumento
                dialogFragment.show(getFragmentManager(), "layout_rating_reporte");
            }
        });
    }

    private void comportamiento_boton_denunciar(Button bDenunciar){
        bDenunciar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // BOTON DENUNCIAR REPORTE
                Bundle args = new Bundle();
                /// AGREGO REPORTE Y USUARIO
                args.putSerializable("selected_report", seleccionado);
                args.putSerializable("logged_in_user", loggedInUser);
                /// INICIO DIALOG CON LOS ATRIBUTOS DEL BUNDLE
                DenunciaReporteDialogFragment dialogFragment = new DenunciaReporteDialogFragment();
                dialogFragment.setArguments(args); // Establece el Bundle como argumento
                dialogFragment.show(getFragmentManager(), "layout_denuciar_reporte");
            }
        });
    }

    private void cargarDatosReporte(){
        /// CONFIGURO DATOS DEL REPORTE
        titulo.setText(seleccionado.getTitulo());
        descripcion.setText(seleccionado.getDescripcion());
        String status_rep = "Estado: " + seleccionado.getEstado().getEstado();
        estado.setText(status_rep);
        color_estado();
        String tipo_rep = "Tipo: " + seleccionado.getTipo().getTipo();
        tipo.setText(tipo_rep);
        fecha.setText(seleccionado.getFecha().toString());
        if(seleccionado.getCant_votos()!=0){
            puntaje.setRating((float) seleccionado.getPuntaje()/seleccionado.getCant_votos());
        }else{
            puntaje.setRating(0);
        }
    }
    private void color_estado(){
        if(seleccionado.getEstado().getEstado().equals("ABIERTO")){
            estado.setTextColor(ContextCompat.getColor(getContext(),R.color.colorVerdeSuave));
            return;
        }
        if(seleccionado.getEstado().getEstado().equals("ATENDIDO")){
            estado.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAzulSuave));
            return;
        }
        if(seleccionado.getEstado().getEstado().equals("PENDIENTE")){
            estado.setTextColor(ContextCompat.getColor(getContext(),R.color.colorNaranjaSuave));
            return;
        }
        if(seleccionado.getEstado().getEstado().equals("DENUNCIADO")){
            estado.setTextColor(ContextCompat.getColor(getContext(),R.color.colorRojoSuave));
            return;
        }
        estado.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetalleReporteViewModel.class);
        // TODO: Use the ViewModel
    }

    private void regresar(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.popBackStack();
    }
}