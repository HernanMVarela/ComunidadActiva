package frgp.utn.edu.ar.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Date;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoDenuncia;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.TipoDenuncia;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.denuncias.DMAGuardarDenunciaReporte;

public class DenunciaReporteDialogFragment extends DialogFragment {
    private EditText etxTituloDenuncia;
    private EditText etxMotivoDenuncia;
    private Reporte selectedReport = null;
    private Usuario loggedInUser = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            selectedReport = (Reporte) args.getSerializable("selected_report");
            loggedInUser = (Usuario) args.getSerializable("logged_in_user");
        }
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_denunciar_reporte, null);

        Button btnAceptar = dialogView.findViewById(R.id.btnConfirmarDenuncia);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelarDenuncia);
        etxTituloDenuncia = dialogView.findViewById(R.id.etxTituloDenuncia);
        etxMotivoDenuncia = dialogView.findViewById(R.id.etxDescripcionDenuncia);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos() && !selectedReport.getEstado().getEstado().equals("DENUNCIADO")){
                    /// CARGA DATOS EN LA DENUNCIA
                    Denuncia nueva = new Denuncia();
                    nueva.setPublicacion(selectedReport);
                    nueva.setTipo(new TipoDenuncia(1,"REPORTE"));
                    nueva.setDenunciante(loggedInUser);
                    nueva.setDescripcion(etxMotivoDenuncia.getText().toString());
                    nueva.setTitulo(etxTituloDenuncia.getText().toString());
                    nueva.setEstado(new EstadoDenuncia(1,"PENDIENTE"));
                    nueva.setFecha_creacion(new Date(System.currentTimeMillis()));

                    /// ITENTA GUARDAR LA DENUNCIA Y CAMBIAR EL ESTADO DEL REPORTE
                    DMAGuardarDenunciaReporte DMAGuardarDen = new DMAGuardarDenunciaReporte(nueva,getContext());
                    DMAGuardarDen.execute();
                }
                // Cierra el diálogo.
                dismiss();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancelar la denuncia y cerrar el diálogo.
                dismiss();
            }
        });

        builder.setView(dialogView);
        return builder.create();
    }

    public boolean validarCampos(){
        if(etxTituloDenuncia.getText().toString().trim().isEmpty() || etxTituloDenuncia.getText().toString().trim().length()<5){
            Toast.makeText(getContext(), "Debes ingresar un título de al menos 4 carácteres", Toast.LENGTH_LONG).show();
            return false;
        }
        if(etxMotivoDenuncia.getText().toString().trim().isEmpty() || etxMotivoDenuncia.getText().toString().trim().length()<5){
            Toast.makeText(getContext(), "Debes ingresar una descripción de al menos 4 carácteres", Toast.LENGTH_LONG).show();
            return false;
        }
        if(loggedInUser == null){
            Toast.makeText(getContext(), "No hay usuario, como llegaste acá?", Toast.LENGTH_LONG).show();
            return false;
        }
        if(selectedReport == null){
            Toast.makeText(getContext(), "No hay reporte, como llegaste acá?", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
