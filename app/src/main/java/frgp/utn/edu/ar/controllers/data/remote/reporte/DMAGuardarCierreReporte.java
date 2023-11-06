package frgp.utn.edu.ar.controllers.data.remote.reporte;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.model.CierreReporte;
import frgp.utn.edu.ar.controllers.data.model.EstadoReporte;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAGuardarCierreReporte extends AsyncTask<String, Void, Boolean> {

    private CierreReporte nuevo;
    private int dataRowModif;

    //Constructor
    public DMAGuardarCierreReporte(CierreReporte nuevo)
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

            String query = "INSERT INTO CIERRES_REPORTE (ID_REPORTE ,ID_USER, FECHA_CIERRE, MOTIVO_CIERRE, IMAGEN,ID_ESTADOCIERRE) VALUES (?,?,?,?,?,?);";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1,nuevo.getReporte().getId());
            ps.setInt(2,nuevo.getUser().getId());
            ps.setTimestamp(3, new java.sql.Timestamp(nuevo.getFechaCierreAsDate().getTime()));
            ps.setString(4,nuevo.getMotivo());
            ps.setBytes(5, byteArray); // Guardar la imagen como un array de bytes
            ps.setInt(6, nuevo.getEstado().getId());

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
