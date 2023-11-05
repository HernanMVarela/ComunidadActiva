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
import frgp.utn.edu.ar.controllers.ui.dialogs.CerrarReporteDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.EditarUsuarioDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.EliminarUsuarioDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.DetalleUsuarioViewModel;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.MailService;
import frgp.utn.edu.ar.controllers.utils.NotificacionService;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class DetalleUsuarioFragment extends Fragment {

    private DetalleUsuarioViewModel mViewModel;
    private TextView username, correo, tipouser, estadouser, fechacreacion, fechabloqueo;
    private TextView nombre, telefono, nacimiento;
    private Button suspender, modificar, eliminar;
    private Usuario loggedInUser = null;
    private Usuario selectedUser = null;
    private SharedPreferencesService preferencesService = new SharedPreferencesService();
    private LogService logService = new LogService();
    private MailService mailService = new MailService();

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
        suspender = view.findViewById(R.id.btn_detalleuser_suspender);
        modificar = view.findViewById(R.id.btn_detalleuser_modificacion);
        eliminar = view.findViewById(R.id.btn_detalleuser_eliminar);
        loggedInUser = preferencesService.getUsuarioData(getContext());
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

        if(selectedUser.getId() == loggedInUser.getId()){
            suspender.setVisibility(View.GONE);
            eliminar.setVisibility(View.GONE);
        }

        if (selectedUser.getEstado().getEstado().equals("ELIMINADO")){
            suspender.setVisibility(View.GONE);
            modificar.setVisibility(View.GONE);
            eliminar.setVisibility(View.GONE);
        }

        boton_suspender(suspender);
        boton_modificar(modificar);
        boton_eliminar(eliminar);
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
        color_estado();
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

    }

    private void color_estado(){
        if(selectedUser.getEstado().getEstado().equals("ACTIVO")){
            estadouser.setTextColor(ContextCompat.getColor(getContext(),R.color.colorVerdeSuave));
            return;
        }
        if(selectedUser.getEstado().getEstado().equals("INACTIVO")){
            estadouser.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAzulSuave));
            return;
        }
        if(selectedUser.getEstado().getEstado().equals("BLOQUEADO")){
            estadouser.setTextColor(ContextCompat.getColor(getContext(),R.color.colorNaranjaSuave));
            return;
        }
        estadouser.setTextColor(ContextCompat.getColor(getContext(),R.color.colorRojoSuave));
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

    /// COMPORTAMIENTO BOTONES
    private void boton_suspender(Button suspender){
        cambiar_boton(suspender);
        suspender.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(selectedUser.getEstado().getEstado().equals("ACTIVO")){
                    /// SI ESTA ACTIVO, SE SUSPENDE AL USUARIO
                    selectedUser.setEstado(new EstadoUsuario(3,"SUSPENDIDO"));
                    logService.log(loggedInUser.getId(), LogsEnum.SUSPENSION_USUARIO, String.format("Suspendiste al usuario %s", selectedUser.getUsername()));
                    mailService.sendMail(selectedUser.getCorreo(), "COMUNIDAD ACTIVA - SUSPENSION DE USUARIO", "Su usuario ha sido suspendido por un administrador.");
                } else if (selectedUser.getEstado().getEstado().equals("INACTIVO") || selectedUser.getEstado().getEstado().equals("SUSPENDIDO") || selectedUser.getEstado().getEstado().equals("BLOQUEADO")){
                    /// SI ESTA INACTIVO, SUSPENDIDO O BLOQUEADO: SE ACTIVA USAURIO
                    selectedUser.setEstado(new EstadoUsuario(1,"ACTIVO"));
                    logService.log(loggedInUser.getId(), LogsEnum.ACTIVACION_USUARIO, String.format("Activaste al usuario %s", selectedUser.getUsername()));
                    mailService.sendMail(selectedUser.getCorreo(), "COMUNIDAD ACTIVA - ACTIVACION DE USUARIO", "Su usuario ha sido activado por un administrador.");
                }
                cargarDatosUsuario();
                cambiar_boton(suspender);
                DMACambiarEstadoUsuario DMACambiarEstadoUser = new DMACambiarEstadoUsuario(selectedUser,getContext());
                DMACambiarEstadoUser.execute();
            }
        });
    }

    private void boton_modificar(Button modificar){
        modificar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(selectedUser.getEstado().getEstado().equals("ACTIVO")){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selected_user", selectedUser);
                    EditarUsuarioDialogFragment dialogFragment = new EditarUsuarioDialogFragment();
                    dialogFragment.setArguments(bundle); // Establece el Bundle como argumento
                    dialogFragment.show(getFragmentManager(), "layout_modificar_usuario");
                }else{
                    Toast.makeText(getContext(), "El usuario no está activo", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void boton_eliminar(Button eliminar){
        eliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!selectedUser.getEstado().getEstado().equals("ELIMINADO")){
                    selectedUser.setEstado(new EstadoUsuario(5,"ELIMINADO"));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selected_user", selectedUser);
                    EliminarUsuarioDialogFragment dialogFragment = new EliminarUsuarioDialogFragment();
                    dialogFragment.setArguments(bundle); // Establece el Bundle como argumento
                    dialogFragment.show(getFragmentManager(), "layout_eliminar_usuario");
                }
            }
        });
    }

}