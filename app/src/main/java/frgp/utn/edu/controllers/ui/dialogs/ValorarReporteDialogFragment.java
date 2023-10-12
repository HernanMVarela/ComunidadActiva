package frgp.utn.edu.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.fragment.app.DialogFragment;

import frgp.utn.edu.ar.controllers.R;

public class ValorarReporteDialogFragment extends DialogFragment {
    Button btnAceptar;
    Button btnCancelar;
    RatingBar rtbValoracion;


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_rating_reporte, null);

        btnAceptar = dialogView.findViewById(R.id.btnConfirmarValoracion);
        btnCancelar = dialogView.findViewById(R.id.btnCancelarValoracion);
        rtbValoracion = dialogView.findViewById(R.id.rtbValoracionReporte);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la valoración del RatingBar.
                float rating = rtbValoracion.getRating();
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
