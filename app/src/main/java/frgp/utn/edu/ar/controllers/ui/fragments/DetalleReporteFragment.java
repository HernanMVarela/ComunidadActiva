package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.ui.activities.VecinoActivity;
import frgp.utn.edu.ar.controllers.ui.dialogs.DenunciaReporteDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.UserDetailDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.ValorarReporteDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.DetalleReporteViewModel;

public class DetalleReporteFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap googlemaplocal;
    private DetalleReporteViewModel mViewModel;
    private TextView titulo, descripcion, estado, fecha, tipo;
    private RatingBar puntaje;
    Button bUsuario;

    private Reporte seleccionado;
        public static DetalleReporteFragment newInstance() {
        return new DetalleReporteFragment();
    }

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
        View view = inflater.inflate(R.layout.fragment_detalle_reporte, container, false);

        // ESCONDE EL BOTON DEL SOBRE
        if(getActivity() instanceof VecinoActivity){
            ((VecinoActivity) getActivity()).botonmensaje.hide();
        }
        titulo = view.findViewById(R.id.titulo_ver_reporte);
        descripcion = view.findViewById(R.id.descripcion_reporte);
        estado = view.findViewById(R.id.estado_ver_reporte);
        fecha = view.findViewById(R.id.reporte_detalle_fecha);
        tipo = view.findViewById(R.id.reporte_det_tipo);
        bUsuario = view.findViewById(R.id.btnUsernameDetalle);
        puntaje = view.findViewById(R.id.detalle_rep_rating);

        Bundle bundle = this.getArguments();
        /// OBTIENE EL REPORTE SELECCIONADO EN LA PANTALLA ANTERIOR
        if (bundle != null) {
            seleccionado = (Reporte) bundle.getSerializable("selected_report");
            /// VALIDA QUE EL REPORTE EXISTA
            if (seleccionado != null) {
                /// CONFIGURO DATOS DEL REPORTE
                titulo.setText(seleccionado.getTitulo());
                descripcion.setText(seleccionado.getTitulo());
                String status_rep = "Estado: " + seleccionado.getEstado().getEstado();
                estado.setText(status_rep);
                String tipo_rep = "Tipo: " + seleccionado.getTipo().getTipo();
                tipo.setText(tipo_rep);
                fecha.setText(seleccionado.getFecha().toString());
                String user_rep = "Detalle del usuario " + seleccionado.getOwner().getUsername();
                bUsuario.setText(user_rep);
                if(seleccionado.getCant_votos()!=0){
                    puntaje.setRating((float) seleccionado.getPuntaje()/seleccionado.getCant_votos());
                }else{
                    puntaje.setRating(0);
                }

            }else {
                /// MODIFICAR PARA REGRESAR A PANTALLA ANTERIOR
                Toast.makeText(this.getContext(), "ERROR AL CARGAR", Toast.LENGTH_SHORT).show();
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
        bUsuario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // BOTON DETALLE DE USUARIO REPORTE
                Usuario user = seleccionado.getOwner();

                UserDetailDialogFragment dialogFragment = UserDetailDialogFragment.newInstance(user);
                dialogFragment.show(getFragmentManager(), "user_detail_reporte");
            }
        });

        Button bSolicitarCierre = view.findViewById(R.id.btnCerrarReporte);
        bSolicitarCierre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // BOTON SOLICITAR CIERRE
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.solicitar_cierre);
            }
        });
        Button bValorar = view.findViewById(R.id.btnValorarReporte);
        bValorar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // BOTON VALORAR REPORTE
                ValorarReporteDialogFragment dialogFragment = new ValorarReporteDialogFragment();
                dialogFragment.show(getFragmentManager(), "layout_rating_reporte");
            }
        });

        Button bDenunciar = view.findViewById(R.id.btnDenunciarReporte);
        bDenunciar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // BOTON VALORAR REPORTE
                DenunciaReporteDialogFragment dialogFragment = new DenunciaReporteDialogFragment();
                dialogFragment.show(getFragmentManager(), "layout_denuciar_reporte");
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetalleReporteViewModel.class);
        // TODO: Use the ViewModel
    }

}