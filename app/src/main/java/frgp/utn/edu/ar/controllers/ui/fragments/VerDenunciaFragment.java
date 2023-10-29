package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMACargarImagenDenuncia;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMACargarImagenReporte;
import frgp.utn.edu.ar.controllers.data.repository.denuncia.DenunciaRepository;
import frgp.utn.edu.ar.controllers.data.repository.usuario.UsuarioRepository;
import frgp.utn.edu.ar.controllers.ui.activities.HomeActivity;
import frgp.utn.edu.ar.controllers.ui.dialogs.UserDetailDialogFragment;
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
    Button btnSuspenderUsuario, btnEliminarPublicacion, btnNotificar;
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
        btnNotificar = view.findViewById(R.id.btnNotificarDenuncia);

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSuspenderUsuario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Usuario usuario = seleccionado.getPublicacion().getOwner();
                // EJECUTAR DMA SUSPENDER USUARIO
                if(usuario.getEstado().getEstado().equals("ACTIVO")) {

                    usuario.setEstado(new EstadoUsuario(3,"SUSPENDIDO"));
                    if(usuarioRepository.modificarUsuario(usuario)){
                        Toast.makeText(getContext(), "Usuario SUSPENDIDO ", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getContext(), "NO SE LOGRO MODIFICAR EL USUARIO", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "El Usuario ya se encuentra: "+usuario.getEstado().getEstado(), Toast.LENGTH_LONG).show();
                }
            }
        });
        btnEliminarPublicacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // EJECUTAR DMA EliminarPublicacion OBTENER EL ESTADO DE LA PUBLICACION si es reporte o proyecto por el tipo de denuncia y ejecutar el DMA que corresponda

                    if(seleccionado.getTipo().getTipo().equals("REPORTE")){
                        Toast.makeText(getContext(), "ES UN REPORTE", Toast.LENGTH_LONG).show();
                    }
                    if(seleccionado.getTipo().getTipo().equals("PROYECTO")){
                        Toast.makeText(getContext(), "ES UN PROYECTO", Toast.LENGTH_LONG).show();
                    }
            }
        });
        btnNotificar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // EJECUTAR DMA notificar
                serviceNotificacion.notificacion(seleccionado.getDenunciante().getId(),"Se notifica el cierre de la Denuncia.");
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
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VerDenunciaViewModel.class);
        // TODO: Use the ViewModel
    }

}