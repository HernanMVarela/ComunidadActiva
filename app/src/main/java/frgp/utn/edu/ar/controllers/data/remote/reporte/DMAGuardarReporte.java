package frgp.utn.edu.ar.controllers.data.remote.reporte;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAGuardarReporte extends AsyncTask<String, Void, Boolean> {

    private Reporte nuevo;
    private int dataRowModif;

    //Constructor
    public DMAGuardarReporte(Reporte nuevo)
    {
        this.nuevo = nuevo;
    }

    @Override
    protected Boolean doInBackground(String... urls) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            nuevo.getImagen().compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            String query = "INSERT INTO REPORTES (TITULO,DESCRIPCION,LATITUD,LONGITUD,IMAGEN,FECHA,CANT_VOTOS,PUNTAJE,ID_USER,ID_TIPO,ID_ESTADO) VALUES (?,?,?,?,?,?,?,?,?,?,?);";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1,nuevo.getTitulo());
            ps.setString(2,nuevo.getDescripcion());
            ps.setDouble(3,nuevo.getLatitud());
            ps.setDouble(4,nuevo.getLongitud());
            ps.setBytes(5, byteArray); // Guardar la imagen como un array de bytes
            ps.setDate(6, new java.sql.Date(nuevo.getFecha().getTime()));
            ps.setInt(7, 0);
            ps.setInt(8, 0);
            ps.setInt(9, nuevo.getOwner().getId());
            ps.setInt(10, nuevo.getTipo().getId());
            ps.setInt(11, nuevo.getEstado().getId());

            dataRowModif = ps.executeUpdate();
            ps.close();
            con.close();
            if(dataRowModif!=0){
                return true;
            }
            return false;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
