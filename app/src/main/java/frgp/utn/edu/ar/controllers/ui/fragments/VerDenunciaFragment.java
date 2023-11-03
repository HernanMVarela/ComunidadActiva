package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoDenuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMACargarImagenDenuncia;
import frgp.utn.edu.ar.controllers.data.repository.denuncia.DenunciaRepository;
import frgp.utn.edu.ar.controllers.data.repository.usuario.UsuarioRepository;
import frgp.utn.edu.ar.controllers.ui.activities.HomeActivity;
import frgp.utn.edu.ar.controllers.ui.dialogs.EliminarPublicacionProyectoDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.EliminarPublicacionReporteDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.NotificarCerrarDenunciaDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.SuspenderUsuarioDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.VerDenunciaViewModel;
import frgp.utn.edu.ar.controllers.utils.NotificacionService;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class VerDenunciaFragment extends Fragment {

    SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private VerDenunciaViewModel mViewModel;
    private TextView titulo;
    private TextView descripcion;
    private TextView denunciante;
    private TextView denunciado;
    private TextView estadoPublicacion;
    private TextView fecha;
    private TextView idPublicacion;
    private ImageView imagenPublicacion;
    private Denuncia seleccionado;
    Button btnSuspenderUsuario, btnEliminarPublicacion, btnNotificarCerrar;
    UsuarioRepository usuarioRepository;

    NotificacionService serviceNotificacion;

    DenunciaRepository denunciaRepository = new DenunciaRepository();

    public static VerDenunciaFragment newInstance() {
        return new VerDenunciaFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_denuncia, container, false);

        serviceNotificacion = new NotificacionService();
        usuarioRepository = new UsuarioRepository();

        // ESCONDE EL BOTON DEL SOBRE
        if(getActivity() instanceof HomeActivity){
            ((HomeActivity) getActivity()).botonmensaje.hide();
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titulo = view.findViewById(R.id.tvTituloDenuncia);
        fecha = view.findViewById(R.id.tvFechaPublicacion);
        descripcion = view.findViewById(R.id.tvDescripcionDenuncia);
        denunciante = view.findViewById(R.id.tvDenunciante);
        denunciado = view.findViewById(R.id.tvDenunciado);
        estadoPublicacion = view.findViewById(R.id.tvEstadoPublicacionDenuncia);
        imagenPublicacion = view.findViewById(R.id.imgPublicacion);
        idPublicacion = view.findViewById(R.id.tvIdPublicacion);
        btnSuspenderUsuario = view.findViewById(R.id.btnSuspenderUs);
        btnEliminarPublicacion = view.findViewById(R.id.btnEliminarPublicacionDenuncia);
        btnNotificarCerrar = view.findViewById(R.id.btnNotificarDenuncia);

        Bundle bundle = this.getArguments();
        /// OBTIENE LA DENUNCIA SELECCIONADA EN LA PANTALLA ANTERIOR
        if (bundle != null) {
            seleccionado = (Denuncia) bundle.getSerializable("selected_denuncia");
            /// VALIDA QUE EL REPORTE EXISTA
            if (seleccionado != null) {
                cargarDatosDenuncia();
            }else {
                /// MODIFICAR PARA REGRESAR A PANTALLA ANTERIOR
                Toast.makeText(this.getContext(), "ERROR AL CARGAR", Toast.LENGTH_LONG).show();
            }
        }
        /// BOTONES
        btnSuspenderUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navegarUsuarioSuspender();
            }
        });
        btnEliminarPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navegarEliminarPublicacion();
            }
        });
        btnNotificarCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navegarCerrarDenuncia();
            }
        });

    }
    private void cargarDatosDenuncia(){
        /// CONFIGURO DATOS DEL REPORTE
        titulo.setText(seleccionado.getTitulo());
        descripcion.setText(seleccionado.getDescripcion());
        denunciante.setText("Denunciante: "+seleccionado.getDenunciante().getUsername());
        denunciado.setText("Denunciado: "+seleccionado.getPublicacion().getOwner().getUsername());
        estadoPublicacion.setText("Estado: " + seleccionado.getEstado().getEstado());
        if(seleccionado.getEstado().getEstado().equals("DENUNCIADO")){
           estadoPublicacion.setBackgroundColor(Color.RED);
        }
        idPublicacion.setText("N°: " +seleccionado.getPublicacion().getId());
        fecha.setText(seleccionado.getFecha_creacion().toString());

        /*String res = denunciaRepository.CargarImagenDenuncia(imagenPublicacion, this.getContext(),seleccionado.getPublicacion().getId(),seleccionado.getTipo().getTipo());

        if(res.equals("")){
            Toast.makeText(this.getContext(), "LA IMAGEN NO SE PUDO CARGAR", Toast.LENGTH_SHORT).show();
        }*/

        DMACargarImagenDenuncia DMAImagenDenuncia = new DMACargarImagenDenuncia(imagenPublicacion, this.getContext(),seleccionado.getPublicacion().getId(),seleccionado.getTipo().getTipo());
        DMAImagenDenuncia.execute();
    }

    public void navegarUsuarioSuspender(){
        if(seleccionado != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("selected_usuarioSuspender", seleccionado);
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_suspender_usuario, bundle);
        }else {
            Toast.makeText(this.getContext(), "Debes seleccionar una Denuncia", Toast.LENGTH_LONG).show();
        }
    }
    public void navegarEliminarPublicacion(){
        if(seleccionado != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("selected_eliminarPublicacion", seleccionado);
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_eliminar_publicacion, bundle);
        }else {
            Toast.makeText(this.getContext(), "Debes seleccionar una Denuncia", Toast.LENGTH_LONG).show();
        }
    }
    public void navegarCerrarDenuncia(){
        if(seleccionado != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("selected_cerrarDenuncia", seleccionado);
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_notificar_denuncia, bundle);
        }else {
            Toast.makeText(this.getContext(), "Debes seleccionar una Denuncia", Toast.LENGTH_LONG).show();
        }
    }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VerDenunciaViewModel.class);
        // TODO: Use the ViewModel
    }

}