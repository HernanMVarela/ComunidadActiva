package frgp.utn.edu.ar.controllers.data.remote.reporte;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import frgp.utn.edu.ar.controllers.data.model.CierreReporte;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAGuardarCierreReporte extends AsyncTask<String, Void, String> {

    private Context context;
    private CierreReporte nuevo;
    private static String result2;
    private int dataRowModif;

    //Constructor
    public DMAGuardarCierreReporte(CierreReporte nuevo, Context ct)
    {
        this.nuevo = nuevo;
        this.context = ct;
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            nuevo.getImagen().compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            String query = "INSERT INTO CIERRES_REPORTE (ID_REPORTE ,ID_USER ,MOTIVO_CIERRE ,FECHA_CIERRE ,IMAGEN,ID_ESTADOCIERRE) VALUES (?,?,?,?,?,?);";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1,nuevo.getReporte().getId());
            ps.setInt(2,nuevo.getUser().getId());
            ps.setString(3,nuevo.getMotivo());
            ps.setDate(4, new java.sql.Date(nuevo.getFecha_cierre().getTime()));
            ps.setBytes(5, byteArray); // Guardar la imagen como un array de bytes
            ps.setInt(6, nuevo.getEstado().getId());

            dataRowModif = ps.executeUpdate();
            result2 = " ";

        }
        catch(Exception e) {
            e.printStackTrace();
            result2 = "Conexion no exitosa";
        }
        return response;

    }
    @Override
    protected void onPostExecute(String response) {
        if(dataRowModif!=0){
            Toast.makeText(context, "Solicitud enviada", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "No se pudo crear la solicitud", Toast.LENGTH_SHORT).show();
        }

    }
}