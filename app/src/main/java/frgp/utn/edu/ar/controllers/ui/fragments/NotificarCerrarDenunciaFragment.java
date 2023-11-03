package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoDenuncia;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.ui.activities.HomeActivity;
import frgp.utn.edu.ar.controllers.ui.dialogs.NotificarCerrarDenunciaDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.NotificarCerrarDenunciaViewModel;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class NotificarCerrarDenunciaFragment extends Fragment {

    private NotificarCerrarDenunciaViewModel mViewModel;

    EditText motivo;
    Button btnNotificarCerrar;
    private Denuncia seleccionado;
    SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private Usuario loggedInUser = null;

    public static NotificarCerrarDenunciaFragment newInstance() {
        return new NotificarCerrarDenunciaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificar_cerrar_denuncia, container, false);

        // ESCONDE EL BOTON DEL SOBRE
        if(getActivity() instanceof HomeActivity){
            ((HomeActivity) getActivity()).botonmensaje.hide();
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        motivo = view.findViewById(R.id.etResolucionDenuncia);
        btnNotificarCerrar = view.findViewById(R.id.btnNotidicarYCerrarDenuncia);
        loggedInUser = sharedPreferences.getUsuarioData(getContext());

        Bundle bundle = this.getArguments();
        /// OBTIENE LA DENUNCIA SELECCIONADA EN LA PANTALLA ANTERIOR
        if (bundle != null) {
            seleccionado = (Denuncia) bundle.getSerializable("selected_cerrarDenuncia");
            /// VALIDA QUE EL REPORTE EXISTA
            if (seleccionado != null) {
                //cargarDatosUsuario();
            }else {
                /// MODIFICAR PARA REGRESAR A PANTALLA ANTERIOR
                Toast.makeText(this.getContext(), "ERROR AL CARGAR", Toast.LENGTH_LONG).show();
            }
        }

        boton_cerrar_denuncia(btnNotificarCerrar);
    }

    private void boton_cerrar_denuncia(Button notificarCerrar){
        notificarCerrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getContext(), seleccionado.getEstado().getEstado(), Toast.LENGTH_SHORT).show();
                //Revisar el tipo de publicacion

                if(!seleccionado.getEstado().getEstado().equals("CERRADA") || !seleccionado.getEstado().getEstado().equals("CANCELADA")){
                    seleccionado.setEstado(new EstadoDenuncia(3,"CERRADA"));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selected_denuncia", seleccionado);
                    bundle.putSerializable("logged_in_user", loggedInUser);
                    bundle.putString("mi_string", motivo.getText().toString());
                    NotificarCerrarDenunciaDialogFragment dialogFragment = new NotificarCerrarDenunciaDialogFragment();
                    dialogFragment.setArguments(bundle); // Establece el Bundle como argumento
                    dialogFragment.show(getFragmentManager(), "layout_notificar_cerrar_denuncia");
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificarCerrarDenunciaViewModel.class);
        // TODO: Use the ViewModel
    }

}