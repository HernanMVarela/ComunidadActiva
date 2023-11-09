package frgp.utn.edu.ar.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMAGuardarDenunciaProyecto;

public class DenunciaProyectoDialogFragment extends DialogFragment {
    private EditText Titulo, Descripcion;
    private int Proyecto=-1, Usuario=-1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            Proyecto = args.getInt("DidProyecto");
            Usuario = args.getInt("DidUsuario");
        }
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_denunciar_proyecto, null);

        Button btnAceptar = dialogView.findViewById(R.id.btnConfirmarDenunciaP);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelarDenunciaP);
        Titulo = dialogView.findViewById(R.id.edTituloDenunciaP);
        Descripcion = dialogView.findViewById(R.id.edDescDenunciaP);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    DMAGuardarDenunciaProyecto DMAGuardar = new DMAGuardarDenunciaProyecto(Proyecto,Usuario,Titulo.getText().toString(),Descripcion.getText().toString(),getContext());
                    DMAGuardar.execute();
                }
                dismiss();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(dialogView);
        return builder.create();
    }

    public boolean validarCampos(){
        if(Titulo.getText().toString().trim().isEmpty() || Titulo.getText().toString().trim().length()<5){
            Toast.makeText(getContext(), "Debes ingresar un título de al menos 4 carácteres", Toast.LENGTH_LONG).show();
            return false;
        }
        if(Descripcion.getText().toString().trim().isEmpty() || Descripcion.getText().toString().trim().length()<5){
            Toast.makeText(getContext(), "Debes ingresar una descripción de al menos 4 carácteres", Toast.LENGTH_LONG).show();
            return false;
        }
        if(Proyecto==-1){
            Toast.makeText(getContext(), "No existe proyecto, disculpa las molestias.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(Usuario==-1){
            Toast.makeText(getContext(), "No existe usuario, disculpa las molestias", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
