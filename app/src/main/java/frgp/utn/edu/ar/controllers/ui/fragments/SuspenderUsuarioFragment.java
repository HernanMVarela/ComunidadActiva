package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMACargarImagenDenuncia;
import frgp.utn.edu.ar.controllers.ui.activities.HomeActivity;
import frgp.utn.edu.ar.controllers.ui.dialogs.SuspenderUsuarioDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.SuspenderUsuarioViewModel;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class SuspenderUsuarioFragment extends Fragment {

    private SuspenderUsuarioViewModel mViewModel;
    private TextView nombreUsuario, tipoUsuario;
    private EditText motivo;
    private Button btnSuspender;
    private Denuncia seleccionado;
    SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private Usuario loggedInUser = null;

    public static SuspenderUsuarioFragment newInstance() {
        return new SuspenderUsuarioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suspender_usuario, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nombreUsuario = view.findViewById(R.id.tvNombreUsuario);
        tipoUsuario = view.findViewById(R.id.tvTipoUsuario);
        motivo = view.findViewById(R.id.etMotivoSuspencion);
        btnSuspender = view.findViewById(R.id.btnSuspenderUsuario);
        loggedInUser = sharedPreferences.getUsuarioData(getContext());

        Bundle bundle = this.getArguments();
        /// OBTIENE LA DENUNCIA SELECCIONADA EN LA PANTALLA ANTERIOR
        if (bundle != null) {
            seleccionado = (Denuncia) bundle.getSerializable("selected_usuarioSuspender");
            /// VALIDA QUE EL REPORTE EXISTA
            if (seleccionado != null) {
                cargarDatosUsuario();
            }else {
                /// MODIFICAR PARA REGRESAR A PANTALLA ANTERIOR
                Toast.makeText(this.getContext(), "ERROR AL CARGAR", Toast.LENGTH_LONG).show();
            }
        }
         boton_suspender_usuario(btnSuspender);
    }

    private void cargarDatosUsuario(){
        /// CONFIGURO DATOS
        nombreUsuario.setText(seleccionado.getPublicacion().getOwner().getUsername());
        tipoUsuario.setText("Tipo: "+seleccionado.getPublicacion().getOwner().getTipo().getTipo());
    }

    private void boton_suspender_usuario(Button suspender){
        suspender.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Revisar el tipo de publicacion
                if(!seleccionado.getPublicacion().getOwner().getEstado().getEstado().equals("ELIMINADO") ||!seleccionado.getPublicacion().getOwner().getEstado().getEstado().equals("SUSPENDIDO")){
                    seleccionado.getPublicacion().getOwner().setEstado(new EstadoUsuario(5,"SUSPENDIDO"));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selected_userPublicacion", seleccionado.getPublicacion().getOwner());
                    bundle.putSerializable("logged_in_user", loggedInUser);
                    bundle.putString("mi_string", motivo.getText().toString());
                    SuspenderUsuarioDialogFragment dialogFragment = new SuspenderUsuarioDialogFragment();
                    dialogFragment.setArguments(bundle); // Establece el Bundle como argumento
                    dialogFragment.show(getFragmentManager(), "layout_suspender_usuario");
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SuspenderUsuarioViewModel.class);
        // TODO: Use the ViewModel
    }

}