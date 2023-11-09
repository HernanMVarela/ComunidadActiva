package frgp.utn.edu.ar.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoDenuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMACambiarEstadoUsuario;
import frgp.utn.edu.ar.controllers.data.repository.denuncia.DenunciaRepository;
import frgp.utn.edu.ar.controllers.data.repository.usuario.UsuarioRepository;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.MailService;
import frgp.utn.edu.ar.controllers.utils.NotificacionService;

public class SuspenderUsuarioDialogFragment extends DialogFragment {

    private Button btnConfirmar;
    private Button btnCancelar;
    private String motivo;
    private Usuario selectedUser = null;
    private Usuario loggedInUser = null;
    private Denuncia denuncia = null;
    private LogService logService = new LogService();
    private MailService mailService = new MailService();
    private NotificacionService serviceNotificacion= new NotificacionService();
    private UsuarioRepository usuarioRepository = new UsuarioRepository();
    private DenunciaRepository denunciaRepository = new DenunciaRepository();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            selectedUser = (Usuario) args.getSerializable("selected_userPublicacion");
            loggedInUser = (Usuario) args.getSerializable("logged_in_user");
            denuncia = (Denuncia) args.getSerializable("selected_denuncia");
            motivo = args.getString("mi_string");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_suspender_usuario, null);

        btnConfirmar = dialogView.findViewById(R.id.btnDialogConfirmarSuspenderUsuario);
        btnCancelar = dialogView.findViewById(R.id.btnDialogCancelarSuspenderUsuario);

        TextView titulo = dialogView.findViewById(R.id.dialog_titulo_suspender_usuario);

        if(selectedUser==null){
            Toast.makeText(getContext(), "ERROR AL CARGAR EL USUARIO", Toast.LENGTH_LONG).show();
            dismiss();
        }
        titulo.setText("¿Desea Suspender al Usuario " + selectedUser.getUsername() + "?");
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EstadoUsuario estado = new EstadoUsuario();
                estado.setId(selectedUser.getEstado().getId());
                estado.setEstado(selectedUser.getEstado().getEstado());
                selectedUser.setEstado(new EstadoUsuario(3,"SUSPENDIDO"));

                EstadoDenuncia estadoDenuncia = new EstadoDenuncia();
                estadoDenuncia.setId(denuncia.getEstado().getId());
                estadoDenuncia.setEstado(denuncia.getEstado().getEstado());
                denuncia.setEstado(new EstadoDenuncia(2,"ATENDIDA"));

                if(usuarioRepository.cambiarEstadoUsuario(selectedUser) && cambiarEstadoDenuncia(denuncia)){
                    logService.log(loggedInUser.getId(), LogsEnum.SUSPENSION_USUARIO, String.format("Suspendiste al usuario %s", selectedUser.getUsername()));
                    mailService.sendMail(selectedUser.getCorreo(), "COMUNIDAD ACTIVA - SUSPENSION DE USUARIO", String.format("Su usuario ha sido suspendido por un MODERADOR. Motivo: %s", motivo));
                    serviceNotificacion.notificacion(selectedUser.getId(),String.format("A sido suspendido por %s por los motivos: %s", loggedInUser.getUsername(), motivo));
                    Toast.makeText(getContext(), "Se suspendio al usuario: "+ selectedUser.getUsername(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), String.format("No se pudo suspender al usuario: %s", selectedUser.getUsername()), Toast.LENGTH_LONG).show();
                    selectedUser.setEstado(estado);
                    denuncia.setEstado(estadoDenuncia);
                }
                dismiss();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.popBackStack();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancelar la valoración y cerrar el diálogo.
                dismiss();
            }
        });

        builder.setView(dialogView);
        return builder.create();
    }

    public boolean cambiarEstadoDenuncia(Denuncia denuncia){
        switch(denuncia.getTipo().getTipo()){
            case "REPORTE":
                return denunciaRepository.cambiarEstadoDenunciaReporte(denuncia);
            case "PROYECTO":
                return denunciaRepository.cambiarEstadoDenunciaProyecto(denuncia);
            default:
                return false;
        }
    }
}
