package frgp.utn.edu.ar.controllers.ui.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import frgp.utn.edu.ar.controllers.data.model.EstadoProyecto;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.model.Voluntario;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAAbandonarProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAActualizarEstadoProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMABuscarUsuarioEnProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMACantidadCuposDisponibles;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMACargarVoluntario;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMACuposDisponibles;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAReUnirseProyecto;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAUnirseAProyecto;
import frgp.utn.edu.ar.controllers.ui.activities.HomeActivity;
import frgp.utn.edu.ar.controllers.ui.dialogs.DenunciaProyectoDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.UserDetailDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.DetalleReporteViewModel;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class DetalleProyectoFragment extends Fragment {

    private SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private DetalleReporteViewModel mViewModel;
    private GoogleMap googlemaplocal;
    private TextView titulo, descripcion, estado, tipo, requerimiento, contacto, cupo;
    private Usuario loggedInUser = null;
    private Proyecto seleccionado;
    private Voluntario existe = null;
    private LogService logService = new LogService();
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
        /// OBTIENE EL PROYECTO SELECCIONADO EN LA PANTALLA ANTERIOR
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
        Button bCancelar = view.findViewById(R.id.btn_detalle_proyecto_cancelar);
        Button bUsuario = view.findViewById(R.id.btn_detalle_proyecto_owner);
        comportamiento_boton_usuario(bUsuario);
        Button bListado = view.findViewById(R.id.btn_detalle_proyecto_participantes);
        comportamiento_boton_participantes(bListado);
        Button bDenunciar = view.findViewById(R.id.btn_detalle_proyecto_denunciar);
        comportamiento_boton_denunciar(bDenunciar);

        bUnirse.setVisibility(View.GONE);
        bDenunciar.setVisibility(View.GONE);
        bFinalizar.setVisibility(View.GONE);
        bCancelar.setVisibility(View.GONE);

        if(seleccionado.getEstado().getEstado().equals("ABIERTO")){
            if(loggedInUser.getId()==seleccionado.getOwner().getId()){
                /// SI EL ESTADO ESTA ABIERTO Y EL USUARIO ES EL OWNER
                bFinalizar.setText("INICIAR PROYECTO");
                Drawable boton_redondeado = ContextCompat.getDrawable(getContext(), R.drawable.boton_redondeado);
                bFinalizar.setBackground(boton_redondeado);
                bFinalizar.setVisibility(View.VISIBLE);
                bFinalizar.setEnabled(true);
                bCancelar.setVisibility(View.VISIBLE);
                bCancelar.setEnabled(true);
                comportamiento_boton_inicar(bFinalizar);
                comportamiento_boton_cancelar(bCancelar);
            }else{
                /// SI EL ESTADO ESTA ABIERTO Y EL USUARIO NO ES EL OWNER
                try {
                    DMABuscarUsuarioEnProyecto DMABuscarUsuario = new DMABuscarUsuarioEnProyecto(loggedInUser.getId(),seleccionado.getId());
                    DMABuscarUsuario.execute();
                    if(DMABuscarUsuario.get()){
                        bUnirse.setText("ABANDONAR PROYECTO");
                    }else{
                        bUnirse.setText("UNIRSE");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                bUnirse.setVisibility(View.VISIBLE);
                bDenunciar.setVisibility(View.VISIBLE);
                bUnirse.setEnabled(true);
                bDenunciar.setEnabled(true);
                comportamiento_boton_unirse(bUnirse);
                comportamiento_boton_denunciar(bDenunciar);
            }
        }
        if(seleccionado.getEstado().getEstado().equals("EN PROCESO")){
            if(loggedInUser.getId()==seleccionado.getOwner().getId()){
                /// SI EL ESTADO ESTA EN PROCESO Y EL USUARIO NO ES EL OWNER
                Drawable boton_redondeado = ContextCompat.getDrawable(getContext(), R.drawable.boton_redondeado_danger);
                bFinalizar.setBackground(boton_redondeado);
                bFinalizar.setVisibility(View.VISIBLE);
                bFinalizar.setEnabled(true);
                comportamiento_boton_finalizar(bFinalizar);
            }else{
                /// SI EL ESTADO ESTA EN PROCESO Y EL USUARIO NO ES EL OWNER
                try {
                    DMABuscarUsuarioEnProyecto DMABuscarUsuario = new DMABuscarUsuarioEnProyecto(loggedInUser.getId(),seleccionado.getId());
                    DMABuscarUsuario.execute();
                    if(DMABuscarUsuario.get()){
                        bUnirse.setText("ABANDONAR PROYECTO");
                    }else{
                        bUnirse.setText("UNIRSE");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                bUnirse.setVisibility(View.VISIBLE);
                bDenunciar.setVisibility(View.VISIBLE);
                bUnirse.setEnabled(true);
                bDenunciar.setEnabled(true);
                comportamiento_boton_unirse(bUnirse);
                comportamiento_boton_denunciar(bDenunciar);
            }
        }
        if(seleccionado.getEstado().getEstado().equals("CANCELADO") ||
           seleccionado.getEstado().getEstado().equals("DENUNCIADO") ||
           seleccionado.getEstado().getEstado().equals("ELIMINADO") ||
           seleccionado.getEstado().getEstado().equals("CERRADO")){
            bUnirse.setEnabled(false);
            bDenunciar.setEnabled(false);
            bFinalizar.setEnabled(false);
            bCancelar.setEnabled(false);
            bUnirse.setVisibility(View.GONE);
            bDenunciar.setVisibility(View.GONE);
            bFinalizar.setVisibility(View.GONE);
            bCancelar.setVisibility(View.GONE);
        }

        DMACantidadCuposDisponibles DMAValidarCupos = new DMACantidadCuposDisponibles(seleccionado.getCupo(),seleccionado.getId());
        try {
            DMAValidarCupos.execute();
            cupo.setText(DMAValidarCupos.get().toString());
            if(DMAValidarCupos.get()<=0){
                bUnirse.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarDatos(){
        titulo.setText(seleccionado.getTitulo());
        descripcion.setText(seleccionado.getDescripcion());
        estado.setText(seleccionado.getEstado().getEstado());
        color_estado();
        tipo.setText(seleccionado.getTipo().getTipo());
        requerimiento.setText(seleccionado.getRequerimientos());
        contacto.setText(seleccionado.getContacto());


    }
    private void color_estado(){
        if(seleccionado.getEstado().getEstado().equals("ABIERTO")){
            estado.setTextColor(ContextCompat.getColor(getContext(),R.color.colorVerdeSuave));
            return;
        }
        if(seleccionado.getEstado().getEstado().equals("FINALIZADO")){
            estado.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAzulSuave));
            return;
        }
        if(seleccionado.getEstado().getEstado().equals("EN PROCESO")){
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
                    /// DMA PARA VALIDAR SI EL USUARIO YA FORMA PARTE DEL PROYECTO
                    DMACargarVoluntario DMAExisteUsuario = new DMACargarVoluntario(loggedInUser.getId(),seleccionado.getId());
                    DMAExisteUsuario.execute();
                    /// SI EXISTE DEVUELVE EL USUARIO COMO VOLUNTARIO - SINO DEVUELVE NULL
                    existe = DMAExisteUsuario.get();
                    if(existe!=null){
                        if(existe.isActivo()){
                            /// SI ESTA EN EL PROYECTO, HABILITA BOTÓN PARA ABANDONAR
                            bUnirse.setText("ABANDONAR PROYECTO");
                            comportamiento_abandonar(bUnirse);
                        }else{
                            /// SI EXISTE PERO ESTÁ INACTIVO (SALIO DEL PROYECTO EN ALGUN MOMENTO), PERMITE EL REINGRESO
                            comportamiento_reunirse(bUnirse);
                        }
                    }else{
                        /// SI NUNCA FUE PARTE DEL PROYECTO, PERMITE UNIRSE NUEVAMENTE
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
        if(seleccionado.getEstado().getEstado().equals("ABIERTO") || seleccionado.getEstado().getEstado().equals("DENUNCIADO") || seleccionado.getEstado().getEstado().equals("EN PROCESO")){
            try {
                /// DMA PARA VALIDAR QUE EL PROYECTO TIENE CUPOS DISPONIBLES
                DMACuposDisponibles DMAValidarCupos = new DMACuposDisponibles(seleccionado.getCupo(),seleccionado.getId());
                DMAValidarCupos.execute();
                /// CONDICION CON EL RESULTADO DE LA CONSULTA
                if(DMAValidarCupos.get()){
                    /// SI TIENE CUPOS DISPONIBLES, SE INTENTA REINGRESAR AL USAURIO
                    DMAUnirseAProyecto DMAUnirseProyecto = new DMAUnirseAProyecto(loggedInUser.getId(),seleccionado.getId());
                    DMAUnirseProyecto.execute();
                    if(DMAUnirseProyecto.get()){
                        /// SI SE INTEGRA CON ÉXITO, CAMBIA EL BOTON Y ACTUALIZA EL ESTADO DEL USUARIO
                        bUnirse.setText("ABANDONAR PROYECTO");
                        Toast.makeText(getContext(),"Voluntario agregado!", Toast.LENGTH_LONG).show();
                        logService.log(loggedInUser.getId(), LogsEnum.UNION_PROYECTO, String.format("Se unió al proyecto %s ", seleccionado.getTitulo()));
                    }else{
                        Toast.makeText(getContext(),"No se pudo agregar al usuario!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(),"No quedan cupos disponibles!", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.e("Error", "No se pudo registrar la operación");
            }
        }else{
            Toast.makeText(getContext(),"El proyecto no está abierto o en proceso", Toast.LENGTH_LONG).show();
        }
    }
    private void comportamiento_reunirse(Button bUnirse){
        if(seleccionado.getEstado().getEstado().equals("ABIERTO") || seleccionado.getEstado().getEstado().equals("DENUNCIADO")){
            try {
                /// DMA PARA VALIDAR QUE EL PROYECTO TIENE CUPOS DISPONIBLES
                DMACuposDisponibles DMAValidarCupos = new DMACuposDisponibles(seleccionado.getCupo(),seleccionado.getId());
                DMAValidarCupos.execute();
                /// CONDICION CON EL RESULTADO DE LA CONSULTA
                if(DMAValidarCupos.get()) {
                    /// SI TIENE CUPOS DISPONIBLES, SE INTENTA REINGRESAR AL USAURIO
                    DMAReUnirseProyecto DMAREUnirse = new DMAReUnirseProyecto(loggedInUser.getId(), seleccionado.getId());
                    DMAREUnirse.execute();
                    if (DMAREUnirse.get()) {
                        /// SI SE REINTEGRA CON ÉXITO, CAMBIA EL BOTON Y ACTUALIZA EL ESTADO DEL USUARIO
                        existe.setActivo(true);
                        bUnirse.setText("ABANDONAR PROYECTO");
                        Toast.makeText(getContext(), "Voluntario reingresado!", Toast.LENGTH_LONG).show();
                        logService.log(loggedInUser.getId(), LogsEnum.UNION_PROYECTO, String.format("Se volvio a unir al proyecto %s ", seleccionado.getTitulo()));
                    } else {
                        Toast.makeText(getContext(), "No se pudo agregar al usuario!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(),"No quedan cupos disponibles!", Toast.LENGTH_LONG).show();
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

                try {
                    seleccionado.setEstado(new EstadoProyecto(2,"FINALIZADO"));
                    DMAActualizarEstadoProyecto DMAFinalizar = new DMAActualizarEstadoProyecto(seleccionado);
                    DMAFinalizar.execute();
                    if(DMAFinalizar.get()){
                        Toast.makeText(getContext(),"Proyecto finalizado!", Toast.LENGTH_LONG).show();
                        logService.log(loggedInUser.getId(), LogsEnum.FINALIZAR_PROYECTO, String.format("Se finalizó el proyecto %s ", seleccionado.getTitulo()));
                        regresar();
                    }else{
                        Toast.makeText(getContext(),"No se pudo finalizar el proyecto", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void comportamiento_boton_cancelar(Button bCancelar){
        bCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    seleccionado.setEstado(new EstadoProyecto(4,"CANCELADO"));
                    DMAActualizarEstadoProyecto DMACancelar = new DMAActualizarEstadoProyecto(seleccionado);
                    DMACancelar.execute();
                    if(DMACancelar.get()){
                        Toast.makeText(getContext(),"Proyecto cancelado!", Toast.LENGTH_LONG).show();
                        logService.log(loggedInUser.getId(), LogsEnum.CANCELAR_PROYECTO, String.format("Se canceló el proyecto %s ", seleccionado.getTitulo()));
                        regresar();
                    }else{
                        Toast.makeText(getContext(),"No se pudo cancelar el proyecto", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void comportamiento_boton_inicar(Button bIniciar){
        bIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    seleccionado.setEstado(new EstadoProyecto(3,"EN PROCESO"));
                    DMAActualizarEstadoProyecto DMAIniciar = new DMAActualizarEstadoProyecto(seleccionado);
                    DMAIniciar.execute();
                    if(DMAIniciar.get()){
                        Toast.makeText(getContext(),"Proyecto iniciado!!", Toast.LENGTH_LONG).show();
                        logService.log(loggedInUser.getId(), LogsEnum.INICIAR_PROYECTO, String.format("Se inició el proyecto %s ", seleccionado.getTitulo()));
                        regresar();
                    }else{
                        Toast.makeText(getContext(),"No se pudo iniciar el proyecto", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
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
                    args.putSerializable("selected_proyect", seleccionado);
                    args.putSerializable("logged_in_user", loggedInUser);
                    DenunciaProyectoDialogFragment dialogFragment = new DenunciaProyectoDialogFragment();
                    dialogFragment.setArguments(args);
                    dialogFragment.show(getFragmentManager(), "layout_denuciar_proyecto");
                }
            }
        });
    }
    private void regresar(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.popBackStack();
    }
}