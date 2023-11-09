package frgp.utn.edu.ar.controllers.data.remote.reporte;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import frgp.utn.edu.ar.controllers.data.remote.DataDB;


public class DMACargarImagenReporte extends AsyncTask<String, Void, Bitmap> {

    private Bitmap bitmap;
    private static String result2;
    private int ID;

    //Constructor
    public DMACargarImagenReporte(int ID) {

        this.ID = ID;
    }
    @Override
    protected Bitmap doInBackground(String... urls) {
        bitmap = null;

        try {
            // Conecta a la base de datos y obtén la URL de la imagen en formato de bytes
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String query = "SELECT R.IMAGEN AS ImagenReporte FROM REPORTES AS R WHERE R.ID=?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, this.ID); // ID del reporte
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                byte[] imageData = rs.getBytes("ImagenReporte");

                // Convierte los bytes a un objeto Bitmap
                Log.i("DB-ACCESS","TOMA IMAGEN DE LA DB");
                bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            }
            preparedStatement.close();
            con.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
