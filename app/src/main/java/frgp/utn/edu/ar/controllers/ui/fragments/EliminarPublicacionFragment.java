package frgp.utn.edu.ar.controllers.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.ui.dialogs.EliminarPublicacionDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.EliminarPublicacionViewModel;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class EliminarPublicacionFragment extends Fragment {

    private EliminarPublicacionViewModel mViewModel;
    private EditText resolucion;
    private Button btnEliminarPublicacion;
    private Denuncia seleccionado;
    private SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private Usuario loggedInUser = null;

    public static EliminarPublicacionFragment newInstance() {
        return new EliminarPublicacionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        /// OBTIENE LA DENUNCIA SELECCIONADA EN LA PANTALLA ANTERIOR
        if (bundle != null) {
            seleccionado = (Denuncia) bundle.getSerializable("selected_eliminarPublicacion");
            /// VALIDA QUE EL REPORTE EXISTA
            if (seleccionado == null) {
                regresar();
            }
        }else{
            regresar();
        }
        /// CARGA DATOS DEL USUARIO
        loggedInUser = sharedPreferences.getUsuarioData(getContext());
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eliminar_publicacion, container, false);

        resolucion = view.findViewById(R.id.etMotivoEliminarPublicacion);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /// BOTON ELIMINAR PUBLICACION Y COMPORTAMIENTO
        btnEliminarPublicacion = view.findViewById(R.id.btnEliminarPublicacion);
        boton_elimiar_publicacion(btnEliminarPublicacion);
    }

    private void boton_elimiar_publicacion(Button eliminarPublicacion){
        eliminarPublicacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(validarCampos()){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selected_publicacionDencia", seleccionado);
                    bundle.putSerializable("logged_in_user", loggedInUser);
                    bundle.putString("motivo_eliminado", resolucion.getText().toString());

                    EliminarPublicacionDialogFragment dialogFragment = new EliminarPublicacionDialogFragment();
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

    private void regresar(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.popBackStack();
    }

    private boolean validarCampos(){
        if(resolucion.getText().toString().trim().isEmpty() || resolucion.getText().toString().trim().length()<5){
            Toast.makeText(getContext(), "Debes ingresar una resolución de al menos 4 carácteres", Toast.LENGTH_LONG).show();
            return false;
        }
        if(resolucion.getText().toString().trim().length()>201){
            Toast.makeText(getContext(), "La resolución debe tener como máximo 200 caractéres", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}