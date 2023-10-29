package frgp.utn.edu.ar.controllers.data.repository.denuncia;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import java.util.Objects;

import frgp.utn.edu.ar.controllers.data.remote.denuncia.DMACargarImagenDenuncia;

public class DenunciaRepository {

    public String CargarImagenDenuncia(ImageView imagen, Context ct, int ID, String tipo){
        DMACargarImagenDenuncia DMAImagenDenuncia = new DMACargarImagenDenuncia(imagen, ct,ID,tipo);
        DMAImagenDenuncia.execute();
        try {
            return DMAImagenDenuncia.get();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }
}
