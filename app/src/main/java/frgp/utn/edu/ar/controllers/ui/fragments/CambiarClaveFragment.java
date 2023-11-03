package frgp.utn.edu.ar.controllers.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.repository.usuario.UsuarioRepository;
import frgp.utn.edu.ar.controllers.databinding.FragmentCambiarClaveBinding;
import frgp.utn.edu.ar.controllers.ui.viewmodels.CambiarClaveViewModel;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.MailService;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class CambiarClaveFragment extends Fragment {
    private FragmentCambiarClaveBinding binding;
    private MailService mailService = new MailService();
    private LogService logger = new LogService();
    private SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private UsuarioRepository usuarioRepository = new UsuarioRepository();
    private Usuario usuario;
    private TextView tvCurrentPass, tvNewPass, tvNewPassRepeat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CambiarClaveViewModel cambiarClaveViewModel =
                new ViewModelProvider(this).get(CambiarClaveViewModel.class);

        binding = FragmentCambiarClaveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        usuario = sharedPreferences.getUsuarioData(this.getContext());
        tvCurrentPass = binding.etPassActual;
        tvNewPass = binding.etNuevaPass;
        tvNewPassRepeat = binding.etConfrimarNuevaPass;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.btnConfirmarCambioPass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    changePass();
                }
                catch (Exception e) {
                    Toast.makeText(getContext(), "Ocurrio un error al cambiar la contraseña", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void changePass() {
        if(!isFormValid()) {
            return;
        }

        usuario.setPassword(tvNewPass.getText().toString());
        usuarioRepository.modificarUsuario(usuario);
        sharedPreferences.saveUsuarioData(this.getContext(), usuario);
        mailService.sendMail(usuario.getCorreo(), "Cambio de contraseña", "Su contraseña fue cambiada correctamente");
        logger.log(usuario.getId(), LogsEnum.CAMBIO_PASS, "Se cambio la contraseña del usuario: " + usuario.getUsername());
        Toast.makeText(this.getContext(), "Se cambio la contraseña correctamente", Toast.LENGTH_LONG).show();
    }


    public boolean isFormValid() {
        if(tvCurrentPass.getText().toString().isEmpty()
        || tvNewPass.getText().toString().isEmpty()
        || tvNewPassRepeat.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!tvCurrentPass.getText().toString().equals(usuario.getPassword())) {
            Toast.makeText(this.getContext(), "La contraseña actual es incorrecta", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!tvNewPass.getText().toString().equals(tvNewPassRepeat.getText().toString())) {
            Toast.makeText(this.getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(tvCurrentPass.getText().toString().equals(tvNewPass.getText().toString())) {
            Toast.makeText(this.getContext(), "La nueva contraseña no puede ser igual a la actual", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (tvNewPass.getText().toString().length() < 6) {
            Toast.makeText(this.getContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}