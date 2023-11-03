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

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.utils.NotificacionService;

public class NotificarCerrarDenunciaDialogFragment extends DialogFragment {
    private Button btnConfirmar;
    private Button btnCancelar;
    private Denuncia selectedDenuncia = null;
    private NotificacionService serviceNotificacion= new NotificacionService();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            selectedDenuncia = (Denuncia) args.getSerializable("selected_denuncia");
        }
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_notificar_cerrar_denuncia, null);

        btnConfirmar = dialogView.findViewById(R.id.btnDialogConfirmarCerrarDenuncia);
        btnCancelar = dialogView.findViewById(R.id.btnDialogCancelarCerrarDenuncia);

        TextView titulo = dialogView.findViewById(R.id.dialog_titulo_cerrar_denuncia);

        if(selectedDenuncia==null){
            Toast.makeText(getContext(), "ERROR AL CARGAR LA DENUNCIA", Toast.LENGTH_LONG).show();
            dismiss();
        }
        titulo.setText("¿Desea Cerrar la Denuncia N° " + selectedDenuncia.getPublicacion().getId()+ "?");
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AGREGAR DMA PARA CERRAR DENUNCIA */

                //DMACambiarEstadoUsuario DMAEstadoUser = new DMACambiarEstadoUsuario(selectedUser,getContext());
                //DMAEstadoUser.execute();

                //serviceNotificacion.notificacion(selectedDenuncia.getDenunciante().getId(),"Se notifica el cierre de la Denuncia.");

                dismiss();
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
}
