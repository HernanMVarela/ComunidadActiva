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
import androidx.fragment.app.FragmentTransaction;
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
        /// COMPORTAMIENTO BOTONES COMUNES
        Button bUsuario = view.findViewById(R.id.btn_detalle_proyecto_owner);
        Button bListado = view.findViewById(R.id.btn_detalle_proyecto_participantes);

        comportamiento_boton_usuario(bUsuario);
        comportamiento_boton_participantes(bListado);

        /// COMPORTAMIENTO BOTONES EXCLUSIVOS
        Button bDenunciar = view.findViewById(R.id.btn_detalle_proyecto_denunciar);
        Button bUnirse = view.findViewById(R.id.btn_detalle_proyecto_unirse);
        Button bFinalizar = view.findViewById(R.id.btn_detalle_proyecto_finalizar);
        Button bCancelar = view.findViewById(R.id.btn_detalle_proyecto_cancelar);
        Button bReunirse = view.findViewById(R.id.btn_detalle_proyecto_reunirse);
        Button bAbandonar = view.findViewById(R.id.btn_detalle_proyecto_abandonar);
        Button bIniciar = view.findViewById(R.id.btn_detalle_proyecto_iniciar);

        bDenunciar.setVisibility(View.GONE);
        bUnirse.setVisibility(View.GONE);
        bFinalizar.setVisibility(View.GONE);
        bCancelar.setVisibility(View.GONE);
        bReunirse.setVisibility(View.GONE);
        bAbandonar.setVisibility(View.GONE);
        bIniciar.setVisibility(View.GONE);

        if(!loggedInUser.getTipo().getTipo().equals("VECINO")){
            return;
        }

        /// VISIBILIDAD DEL BOTON DENUNCIAR
        if(loggedInUser.getId()!=seleccionado.getOwner().getId() && !seleccionado.getEstado().getEstado().equals("DENUNCIADO") && !seleccionado.getEstado().getEstado().equals("CANCELADO")){
            bDenunciar.setVisibility(View.VISIBLE);
            comportamiento_boton_denunciar(bDenunciar);
        }else{
            bDenunciar.setVisibility(View.GONE);
        }

        if(loggedInUser.getId()!=seleccionado.getOwner().getId() || seleccionado.getEstado().getEstado().equals("ABIERTO")|| seleccionado.getEstado().getEstado().equals("EN PROCESO")){
            existe_usuario();
            if(existe!=null){
                if(existe.isActivo()){
                    /// VISIBILIDAD DEL BOTON ABANDONAR
                    bAbandonar.setVisibility(View.VISIBLE);
                    comportamiento_boton_abandonar(bAbandonar);
                }else{
                    /// VISIBILIDAD DEL BOTON REUNIRSE
                    bReunirse.setVisibility(View.VISIBLE);
                    comportamiento_boton_reunirse(bReunirse);
                }
            }else{
                /// VISIBILIDAD DEL BOTON UNIRSE
                bUnirse.setVisibility(View.VISIBLE);
                comportamiento_boton_unirse(bUnirse);
            }
        }else{
            bUnirse.setVisibility(View.GONE);
        }

        if(loggedInUser.getId() == seleccionado.getOwner().getId()){
            bAbandonar.setVisibility(View.GONE);
            if(seleccionado.getEstado().getEstado().equals("ABIERTO")){
                bCancelar.setVisibility(View.VISIBLE);
                bIniciar.setVisibility(View.VISIBLE);
                comportamiento_boton_inicar(bIniciar);
                comportamiento_boton_cancelar(bCancelar);
            }else{
                bCancelar.setVisibility(View.GONE);
                bIniciar.setVisibility(View.GONE);
            }
            if(seleccionado.getEstado().getEstado().equals("EN PROCESO")){
                comportamiento_boton_finalizar(bFinalizar);
                bFinalizar.setVisibility(View.VISIBLE);
            }else{
                bFinalizar.setVisibility(View.GONE);
            }
        }else{
            bCancelar.setVisibility(View.GONE);
            bIniciar.setVisibility(View.GONE);
            bFinalizar.setVisibility(View.GONE);
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
    private void comportamiento_boton_unirse(Button bUnirse){
        bUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar_cupos()){
                    /// SI TIENE CUPOS DISPONIBLES, SE INTENTA REINGRESAR AL USAURIO
                    DMAUnirseAProyecto DMAUnirseProyecto = new DMAUnirseAProyecto(loggedInUser.getId(),seleccionado.getId());
                    DMAUnirseProyecto.execute();
                    try {
                        if(DMAUnirseProyecto.get()){
                            /// SI SE INTEGRA CON ÉXITO, CAMBIA EL BOTON Y ACTUALIZA EL ESTADO DEL USUARIO
                            Toast.makeText(getContext(),"Voluntario agregado!", Toast.LENGTH_LONG).show();
                            logService.log(loggedInUser.getId(), LogsEnum.UNION_PROYECTO, String.format("Se unió al proyecto %s ", seleccionado.getTitulo()));
                            recarga_fragmento();
                        }else{
                            Toast.makeText(getContext(),"No se pudo agregar al usuario!", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                    e.printStackTrace();
                    Log.e("Error", "No se pudo registrar la operación");
                    }
                }else{
                    Toast.makeText(getContext(),"No quedan cupos disponibles!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void comportamiento_boton_abandonar(Button bAbandonar){
        bAbandonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DMAAbandonarProyecto DMAAbandonar = new DMAAbandonarProyecto(loggedInUser.getId(),seleccionado.getId());
                    DMAAbandonar.execute();
                    if(DMAAbandonar.get()){
                        Toast.makeText(getContext(),"Has abandonado el proyecto!", Toast.LENGTH_LONG).show();
                        logService.log(loggedInUser.getId(), LogsEnum.ABANDONO_PROYECTO, String.format("Abandonó el proyecto %s ", seleccionado.getTitulo()));
                        recarga_fragmento();
                    }else{
                        Toast.makeText(getContext(),"Ha ocurrido un error", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    private void comportamiento_boton_reunirse(Button bReunirse){
        bReunirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validar_cupos()) {
                    try {
                        /// SI TIENE CUPOS DISPONIBLES, SE INTENTA REINGRESAR AL USAURIO
                        DMAReUnirseProyecto DMAREUnirse = new DMAReUnirseProyecto(loggedInUser.getId(), seleccionado.getId());
                        DMAREUnirse.execute();
                        if (DMAREUnirse.get()) {
                            /// SI SE REINTEGRA CON ÉXITO, CAMBIA EL BOTON Y ACTUALIZA EL ESTADO DEL USUARIO
                            Toast.makeText(getContext(), "Voluntario reingresado!", Toast.LENGTH_LONG).show();
                            logService.log(loggedInUser.getId(), LogsEnum.UNION_PROYECTO, String.format("Se volvio a unir al proyecto %s ", seleccionado.getTitulo()));
                            recarga_fragmento();
                        } else {
                            Toast.makeText(getContext(), "No se pudo agregar al usuario!", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Error", "No se pudo registrar la operación");
                    }
                } else {
                    Toast.makeText(getContext(), "No quedan cupos disponibles!", Toast.LENGTH_LONG).show();
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
                        recarga_fragmento();
                    }else{
                        Toast.makeText(getContext(),"No se pudo iniciar el proyecto", Toast.LENGTH_LONG).show();
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
                        recarga_fragmento();
                    }else{
                        Toast.makeText(getContext(),"No se pudo cancelar el proyecto", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    private void comportamiento_boton_finalizar(Button bFinalizar){
        bFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    /// SE ACTUALIZA EL ESTADO DEL PROYECTO
                    seleccionado.setEstado(new EstadoProyecto(2,"FINALIZADO"));
                    DMAActualizarEstadoProyecto DMAFinalizar = new DMAActualizarEstadoProyecto(seleccionado);
                    DMAFinalizar.execute();
                    if(DMAFinalizar.get()){
                        /// SE GENERAN LOS LOGS Y SE RECARGA EL FRAGMENTO
                        Toast.makeText(getContext(),"Proyecto finalizado!", Toast.LENGTH_LONG).show();
                        logService.log(loggedInUser.getId(), LogsEnum.FINALIZAR_PROYECTO, String.format("Se finalizó el proyecto %s ", seleccionado.getTitulo()));
                        recarga_fragmento();
                    }else{
                        Toast.makeText(getContext(),"No se pudo finalizar el proyecto", Toast.LENGTH_LONG).show();
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
                    /// SE LLAMA AL DIALOG PARA GENERAR DENUNCIA
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

    /// METODOS AUXILIARES
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
    private boolean validar_cupos(){
        try {
            /// DMA PARA VALIDAR QUE EL PROYECTO TIENE CUPOS DISPONIBLES
            DMACuposDisponibles DMAValidarCupos = new DMACuposDisponibles(seleccionado.getCupo(),seleccionado.getId());
            DMAValidarCupos.execute();
            /// CONDICION CON EL RESULTADO DE LA CONSULTA
            return DMAValidarCupos.get();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private void existe_usuario(){
        try {
            /// DMA PARA VALIDAR SI EL USUARIO YA FORMA PARTE DEL PROYECTO
            DMACargarVoluntario DMAExisteUsuario = new DMACargarVoluntario(loggedInUser.getId(),seleccionado.getId());
            DMAExisteUsuario.execute();
            /// SI EXISTE DEVUELVE EL USUARIO COMO VOLUNTARIO - SINO DEVUELVE NULL
            existe = DMAExisteUsuario.get();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void regresar(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.popBackStack();
    }
    private void recarga_fragmento(){
        // SE CARGA EN BUNDLE EL PROYECTO ACTUAL
        Bundle bundle = new Bundle();
        bundle.putSerializable("proyectoactual", seleccionado);

        // SE RECREA EL FRAGMENTO PARA ACTUALIZAR ESTADOS Y COMPORTAMIENTO DE BOTONES
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        DetalleProyectoFragment nuevoFragmento = new DetalleProyectoFragment();
        nuevoFragmento.setArguments(bundle);
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, nuevoFragmento);
        fragmentTransaction.addToBackStack(null);  // Opcional: agrega a la pila de retroceso
        fragmentTransaction.commit();
    }
}