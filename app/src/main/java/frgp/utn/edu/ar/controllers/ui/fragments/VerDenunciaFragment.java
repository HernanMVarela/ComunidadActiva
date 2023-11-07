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
import frgp.utn.edu.ar.controllers.ui.dialogs.AtenderDenunciaDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.EliminarPublicacionProyectoDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.EliminarPublicacionReporteDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.NotificarCerrarDenunciaDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.SuspenderUsuarioDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.UserDetailDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.ValorarReporteDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.VerDenunciaViewModel;
import frgp.utn.edu.ar.controllers.utils.NotificacionService;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class VerDenunciaFragment extends Fragment {

    private SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private VerDenunciaViewModel mViewModel;
    private TextView tvTituloPublicacion, tvTituloDenuncia, tvDescripcion, tvEstadoDenuncia, tvFecha, tvTipoPublic;
    private Denuncia seleccionado;
    private NotificacionService serviceNotificacion;
    private DenunciaRepository denunciaRepository = new DenunciaRepository();
    public static VerDenunciaFragment newInstance() {
        return new VerDenunciaFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        /// OBTIENE LA DENUNCIA SELECCIONADA EN LA PANTALLA ANTERIOR
        if (bundle != null) {
            seleccionado = (Denuncia) bundle.getSerializable("selected_denuncia");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_denuncia, container, false);

        serviceNotificacion = new NotificacionService();

        tvTituloDenuncia = view.findViewById(R.id.txtTituloDenuncia);
        tvTituloPublicacion = view.findViewById(R.id.txtTituloPublicacion);
        tvFecha = view.findViewById(R.id.tvFechaPublicacion);
        tvDescripcion = view.findViewById(R.id.txtDescripcionDenuncia);
        tvEstadoDenuncia = view.findViewById(R.id.txtEstadoDenuncia);
        tvTipoPublic = view.findViewById(R.id.txtTipoPublicacion);

        // ESCONDE EL BOTON DEL SOBRE
        if(getActivity() instanceof HomeActivity){
            ((HomeActivity) getActivity()).botonmensaje.hide();
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnSuspenderUsuario = view.findViewById(R.id.btnSuspenderUs);
        Button btnEliminarPublicacion = view.findViewById(R.id.btnEliminarPublicacionDenuncia);
        Button btnNotificarCerrar = view.findViewById(R.id.btnNotificarDenuncia);
        Button btnDenunciante = view.findViewById(R.id.btnDenuncianteUser);
        Button btnDenunciado = view.findViewById(R.id.btnDenunciadoUser);

        if (seleccionado != null) {
            cargarDatosDenuncia();
        }else {
            /// MODIFICAR PARA REGRESAR A PANTALLA ANTERIOR
            Toast.makeText(this.getContext(), "ERROR AL CARGAR", Toast.LENGTH_LONG).show();
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            navController.popBackStack();
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
                navegarAtenderDenuncia();
            }
        });

        btnDenunciante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navegarDetalleUsuario(seleccionado.getDenunciante());
            }
        });

        btnDenunciado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navegarDetalleUsuario(seleccionado.getPublicacion().getOwner());
            }
        });

    }
    private void cargarDatosDenuncia(){
        /// CONFIGURO DATOS DEL REPORTE
        tvTituloDenuncia.setText(seleccionado.getTitulo());
        tvTituloPublicacion.setText(seleccionado.getPublicacion().getTitulo());
        tvDescripcion.setText(seleccionado.getDescripcion());

        tvEstadoDenuncia.setText("Estado: " + seleccionado.getEstado().getEstado());
        if(seleccionado.getEstado().getEstado().equals("DENUNCIADO")){
            tvEstadoDenuncia.setBackgroundColor(Color.RED);
        }
        tvTipoPublic.setText("Tipo de publicación: " +seleccionado.getTipo().getTipo());
        tvFecha.setText(seleccionado.getFecha_creacion().toString());

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
    public void navegarAtenderDenuncia(){
        if(seleccionado != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("selected_denuncia", seleccionado);
            AtenderDenunciaDialogFragment dialogFragment = new AtenderDenunciaDialogFragment();
            dialogFragment.setArguments(bundle); // Establece el Bundle como argumento
            dialogFragment.show(getFragmentManager(), "layout_atender_denuncia");
        }else {
            Toast.makeText(this.getContext(), "Debes seleccionar una Denuncia", Toast.LENGTH_LONG).show();
        }
    }

    public void navegarDetalleUsuario(Usuario user){
        UserDetailDialogFragment dialogFragment = UserDetailDialogFragment.newInstance(user);
        dialogFragment.show(getFragmentManager(), "user_detail");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VerDenunciaViewModel.class);
        // TODO: Use the ViewModel
    }
}