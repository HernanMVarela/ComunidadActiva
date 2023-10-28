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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMACambiarEstadoUsuario;
import frgp.utn.edu.ar.controllers.ui.activities.HomeActivity;
import frgp.utn.edu.ar.controllers.ui.viewmodels.DetalleUsuarioViewModel;

public class DetalleUsuarioFragment extends Fragment {

    private DetalleUsuarioViewModel mViewModel;
    private TextView username, correo, tipouser, estadouser, fechacreacion, fechabloqueo;
    private TextView nombre, telefono, nacimiento;
    private Button suspender, notificar, cambiarclave;
    private Usuario selectedUser = null;

    public static DetalleUsuarioFragment newInstance() {
        return new DetalleUsuarioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_usuario, container, false);
        // ESCONDE EL BOTON DEL SOBRE
        if(getActivity() instanceof HomeActivity){
            ((HomeActivity) getActivity()).botonmensaje.hide();
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /// DATOS DE USUARIO
        username = view.findViewById(R.id.txt_detalleuser_username);
        correo = view.findViewById(R.id.txt_detalleuser_correo);
        tipouser = view.findViewById(R.id.txt_detalleuser_tipouser);
        estadouser = view.findViewById(R.id.txt_detalleuser_estadouser);
        fechacreacion = view.findViewById(R.id.txt_detalleuser_creacion);
        fechabloqueo = view.findViewById(R.id.txt_detalleuser_bloqueo);

        /// DATOS PERSONALES
        nombre = view.findViewById(R.id.txt_detalleuser_nombre);
        telefono = view.findViewById(R.id.txt_detalleuser_telefono);
        nacimiento = view.findViewById(R.id.txt_detalleuser_nacimiento);

        /// ASIGNACION DE VALORES
        Bundle bundle = this.getArguments();
        /// OBTIENE EL USUARIO SELECCIONADO EN LA PANTALLA ANTERIOR
        if (bundle != null) {
            selectedUser = (Usuario) bundle.getSerializable("selected_user");
            /// VALIDA QUE EL USUARIO EXISTA
            if (selectedUser != null) {
                cargarDatosUsuario();
            }else {
                /// MODIFICAR PARA REGRESAR A PANTALLA ANTERIOR
                Toast.makeText(this.getContext(), "ERROR AL CARGAR USUARIO", Toast.LENGTH_LONG).show();
                navegarAtras();
            }
        }

        /// BOTON SUSPENDER / ACTIVAR
        suspender = view.findViewById(R.id.btn_detalleuser_suspender);
        boton_suspender(suspender);
        /// BOTON SUSPENDER / ACTIVAR
        notificar = view.findViewById(R.id.btn_detalleuser_notificacion);

        /// BOTON SUSPENDER / ACTIVAR
        cambiarclave = view.findViewById(R.id.btn_detalleuser_contraseña);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetalleUsuarioViewModel.class);
        // TODO: Use the ViewModel
    }

    private void cargarDatosUsuario(){
        username.setText(selectedUser.getUsername());
        correo.setText(selectedUser.getCorreo());
        tipouser.setText("Tipo de usuario: " + selectedUser.getTipo().getTipo());
        estadouser.setText("Estado: " + selectedUser.getEstado().getEstado());
        fechacreacion.setText(selectedUser.getFecha_alta().toString());
        if(selectedUser.getFecha_bloqueo()==null){
            fechabloqueo.setText("Nunca");
        } else {
            fechabloqueo.setText(selectedUser.getFecha_bloqueo().toString());
        }
        if(selectedUser.getNombre()==null || selectedUser.getApellido()==null || selectedUser.getNombre().isEmpty() || selectedUser.getApellido().isEmpty()){
            nombre.setText("Sin nombre");
        } else {
            nombre.setText(selectedUser.getNombre() + " " + selectedUser.getApellido());
        }
        if(selectedUser.getTelefono() == null || selectedUser.getTelefono().isEmpty()){
            telefono.setText("Sin teléfono");
        } else {
            telefono.setText("Teléfono: " + selectedUser.getTelefono());
        }
        if(selectedUser.getFecha_nac() == null){
            nacimiento.setText("Sin fecha de nacimiento");
        } else {
            nacimiento.setText("Nacimiento: " + selectedUser.getFecha_nac().toString());
        }
        if (selectedUser.getEstado().getEstado().equals("SUSPENDIDO")){
            estadouser.setTextColor(ContextCompat.getColor(getContext(), R.color.danger));
        } else if (selectedUser.getEstado().getEstado().equals("BLOQUEADO")){
            estadouser.setTextColor(ContextCompat.getColor(getContext(), R.color.warning));
        } else if (selectedUser.getEstado().getEstado().equals("ACTIVO")){
            estadouser.setTextColor(ContextCompat.getColor(getContext(), R.color.teal_700));
        } else {
            estadouser.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }
    }

    private void navegarAtras(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.popBackStack();
    }

    private void cambiar_boton(Button suspender){
        if(selectedUser.getEstado().getEstado().equals("ACTIVO")){
            suspender.setText("Suspender");
        } else if (selectedUser.getEstado().getEstado().equals("INACTIVO") || selectedUser.getEstado().getEstado().equals("SUSPENDIDO")){
            suspender.setText("Activar");
        } else if (selectedUser.getEstado().getEstado().equals("BLOQUEADO")){
            suspender.setText("Desbloquear");
        }
    }
    private void boton_suspender(Button suspender){
        cambiar_boton(suspender);
        suspender.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(selectedUser.getEstado().getEstado().equals("ACTIVO")){
                    /// SI ESTA ACTIVO, SE SUSPENDE AL USUARIO
                    selectedUser.setEstado(new EstadoUsuario(3,"SUSPENDIDO"));
                } else if (selectedUser.getEstado().getEstado().equals("INACTIVO") || selectedUser.getEstado().getEstado().equals("SUSPENDIDO") || selectedUser.getEstado().getEstado().equals("BLOQUEADO")){
                    /// SI ESTA INACTIVO, SUSPENDIDO O BLOQUEADO: SE ACTIVA USAURIO
                    selectedUser.setEstado(new EstadoUsuario(1,"ACTIVO"));
                }
                cargarDatosUsuario();
                cambiar_boton(suspender);
                DMACambiarEstadoUsuario DMACambiarEstadoUser = new DMACambiarEstadoUsuario(selectedUser,getContext());
                DMACambiarEstadoUser.execute();
            }
        });
    }
}