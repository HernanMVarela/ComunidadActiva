package frgp.utn.edu.ar.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.ReseniaReporte;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMAActualizarVotosReporte;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMAGuardarResenia;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMAVerificarUsuarioVoto;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMAModificarPuntajeUsuario;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.NotificacionService;

public class AyudaFAQDialogFragment extends DialogFragment {

    private Button btnCancelar;
    private String titulo, subtitulo, paso1, paso2, paso3, paso4;
    private TextView txtTitulo, txtSubtitulo, txtPaso1, txtPaso2, txtPaso3, txtPaso4;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            titulo = (String) args.getSerializable("ayuda_titulo");
            subtitulo = (String) args.getSerializable("ayuda_subtitulo");
            paso1 = (String) args.getSerializable("ayuda_paso1");
            paso2 = (String) args.getSerializable("ayuda_paso2");
            paso3 = (String) args.getSerializable("ayuda_paso3");
            paso4 = (String) args.getSerializable("ayuda_paso4");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_dialog_ayuda, null);

        btnCancelar = dialogView.findViewById(R.id.btn_cerrar_ayuda_dialog);
        txtTitulo = dialogView.findViewById(R.id.ayuda_titulo);
        txtSubtitulo = dialogView.findViewById(R.id.ayuda_subtitulo);
        txtPaso1 = dialogView.findViewById(R.id.ayuda_paso_1);
        txtPaso2 = dialogView.findViewById(R.id.ayuda_paso_2);
        txtPaso3 = dialogView.findViewById(R.id.ayuda_paso_3);
        txtPaso4= dialogView.findViewById(R.id.ayuda_paso_4);

        txtTitulo.setText(titulo);
        txtSubtitulo.setText(subtitulo);
        txtPaso1.setText(paso1);
        txtPaso2.setText(paso2);
        txtPaso3.setText(paso3);
        txtPaso4.setText(paso4);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el diálogo.
                dismiss();
            }
        });
        builder.setView(dialogView);
        return builder.create();
    }


}
