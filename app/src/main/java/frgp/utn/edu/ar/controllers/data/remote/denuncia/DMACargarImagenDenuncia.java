package frgp.utn.edu.ar.controllers.data.remote.denuncia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import frgp.utn.edu.ar.controllers.data.model.TipoDenuncia;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMACargarImagenDenuncia extends AsyncTask<String, Void, String> {
    private Context context;
    ImageView imagen;
    private Bitmap bitmap;
    private static String result2;
    private int ID;
    private TipoDenuncia tipo;

    public DMACargarImagenDenuncia(ImageView imagen, Context ct, int ID, TipoDenuncia tipo) {
        this.context = ct;
        this.imagen = imagen;
        this.ID = ID;
        this.tipo = tipo;
    }

    @Override
    protected String doInBackground(String... strings) {

        bitmap = null;

        try {
            // Conecta a la base de datos y obtén la URL de la imagen en formato de bytes
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            //  QUERY CON EL ID DE LA PUBLICACION, PUEDE SER REPORTE O PROYECTO
            String query = "SELECT R.IMAGEN AS Imagen FROM "+ tipo.getTipo() +" AS R WHERE R.ID=?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, this.ID); // ID del reporte
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                byte[] imageData = rs.getBytes("Imagen");

                // Convierte los bytes a un objeto Bitmap
                Log.i("DB-ACCESS","TOMA IMAGEN DE LA DB");
                bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result2;
    }
}
