package frgp.utn.edu.ar.controllers.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMACargarImagenReporte;

public class ImagenReporteDialogFragment extends DialogFragment {
    private TextView titulo;
    private ImageView imagen;

    // Método estático para crear una instancia del fragmento con argumentos
    public static ImagenReporteDialogFragment newInstance(int reporte_id) {
        ImagenReporteDialogFragment fragment = new ImagenReporteDialogFragment();

        Bundle args = new Bundle();
        args.putInt("reporte_id", reporte_id);
        fragment.setArguments(args);
        return fragment;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogTheme_transparent);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_reporte_imagen, null);

        titulo = dialogView.findViewById(R.id.dialog_imagen_reporte_titulo);
        imagen = dialogView.findViewById(R.id.dialog_imagen_reporte_full);


        // Obtén los datos del usuario de los argumentos
        Bundle args = getArguments();
        if (args != null) {
            int reporte_id = args.getInt("reporte_id");
            carga_datos(reporte_id);
            titulo.setText("Imagen del reporte " + reporte_id);
        }else{
            dismiss();
        }

        builder.setView(dialogView);
        return builder.create();
    }

    private void carga_datos(int id){
        try {
            DMACargarImagenReporte DMACargarImagen = new DMACargarImagenReporte(id);
            DMACargarImagen.execute();
            Bitmap imagen_cargada = DMACargarImagen.get();
            if(imagen_cargada!=null){
                int maxWidth = 700; // El ancho máximo deseado en píxeles
                int maxHeight = 700; // El alto máximo deseado en píxeles

                int originalWidth = imagen_cargada.getWidth();
                int originalHeight = imagen_cargada.getHeight();

                float widthRatio = (float) maxWidth / originalWidth;
                float heightRatio = (float) maxHeight / originalHeight;

// Elige el factor de escala más pequeño para mantener la relación de aspecto
                float scaleFactor = Math.min(widthRatio, heightRatio);

                int newWidth = (int) (originalWidth * scaleFactor);
                int newHeight = (int) (originalHeight * scaleFactor);

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(imagen_cargada, newWidth, newHeight, true);
                // Carga la imagen escalada en el ImageView
                imagen.setImageBitmap(scaledBitmap);
            }else{
                dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
