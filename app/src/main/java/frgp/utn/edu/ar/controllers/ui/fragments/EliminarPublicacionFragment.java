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
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.ui.activities.HomeActivity;
import frgp.utn.edu.ar.controllers.ui.dialogs.EliminarPublicacionProyectoDialogFragment;
import frgp.utn.edu.ar.controllers.ui.dialogs.EliminarPublicacionReporteDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.EliminarPublicacionViewModel;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class EliminarPublicacionFragment extends Fragment {

    private EliminarPublicacionViewModel mViewModel;
    EditText motivo;
    Button btnEliminarPublicacion;
    private Denuncia seleccionado;
    SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private Usuario loggedInUser = null;

    public static EliminarPublicacionFragment newInstance() {
        return new EliminarPublicacionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eliminar_publicacion, container, false);

        // ESCONDE EL BOTON DEL SOBRE
        if(getActivity() instanceof HomeActivity){
            ((HomeActivity) getActivity()).botonmensaje.hide();
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        motivo = view.findViewById(R.id.etMotivoEliminarPublicacion);
        btnEliminarPublicacion = view.findViewById(R.id.btnEliminarPublicacion);
        loggedInUser = sharedPreferences.getUsuarioData(getContext());

        Bundle bundle = this.getArguments();
        /// OBTIENE LA DENUNCIA SELECCIONADA EN LA PANTALLA ANTERIOR
        if (bundle != null) {
            seleccionado = (Denuncia) bundle.getSerializable("selected_eliminarPublicacion");
            /// VALIDA QUE EL REPORTE EXISTA
            if (seleccionado != null) {
                //cargarDatosUsuario();
            }else {
                /// MODIFICAR PARA REGRESAR A PANTALLA ANTERIOR
                Toast.makeText(this.getContext(), "ERROR AL CARGAR", Toast.LENGTH_LONG).show();
            }
        }
         boton_elimiar_publicacion(btnEliminarPublicacion);
    }

    private void boton_elimiar_publicacion(Button eliminarPublicacion){
        eliminarPublicacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(seleccionado.getTipo().getTipo().equals("REPORTE")){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selected_publicacionDencia", seleccionado);
                    bundle.putSerializable("logged_in_user", loggedInUser);
                    bundle.putString("mi_string", motivo.getText().toString());
                    EliminarPublicacionReporteDialogFragment dialogFragment = new EliminarPublicacionReporteDialogFragment();
                    dialogFragment.setArguments(bundle); // Establece el Bundle como argumento
                    dialogFragment.show(getFragmentManager(), "layout_eliminar_publicacion");
                }
                if(seleccionado.getTipo().getTipo().equals("PROYECTO")){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selected_publicacionDencia", seleccionado);
                    bundle.putSerializable("logged_in_user", loggedInUser);
                    bundle.putString("mi_string", motivo.getText().toString());
                    EliminarPublicacionProyectoDialogFragment dialogFragment = new EliminarPublicacionProyectoDialogFragment();
                    dialogFragment.setArguments(bundle); // Establece el Bundle como argumento
                    dialogFragment.show(getFragmentManager(), "layout_eliminar_publicacion");
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EliminarPublicacionViewModel.class);
        // TODO: Use the ViewModel
    }

}