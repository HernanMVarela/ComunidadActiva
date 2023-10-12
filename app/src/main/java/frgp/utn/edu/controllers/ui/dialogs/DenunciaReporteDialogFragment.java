package frgp.utn.edu.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import frgp.utn.edu.ar.controllers.R;

public class DenunciaReporteDialogFragment extends DialogFragment {
    Button btnAceptar;
    Button btnCancelar;
    EditText etxTituloDenuncia;
    EditText etxMotivoDenuncia;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_denunciar_reporte, null);

        btnAceptar = dialogView.findViewById(R.id.btnConfirmarDenuncia);
        btnCancelar = dialogView.findViewById(R.id.btnCancelarDenuncia);
        etxTituloDenuncia = dialogView.findViewById(R.id.etxTituloDenuncia);
        etxMotivoDenuncia = dialogView.findViewById(R.id.etxDescripcionDenuncia);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la valoración del RatingBar.

                // Cierra el diálogo.
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
