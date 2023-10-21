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
import frgp.utn.edu.ar.controllers.data.model.ReseniaReporte;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAGuardarResenia extends AsyncTask<String, Void, String> {

    private Context context;
    private ReseniaReporte nuevo;
    private static String result2;
    private int dataRowModif;

    //Constructor
    public DMAGuardarResenia(ReseniaReporte nuevo, Context ct)
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

            String query = "INSERT INTO RESENIA_REPORTE (ID_REPORTE ,ID_VOTANTE ,PUNTAJE) VALUES (?,?,?);";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1,nuevo.getReporte().getId());
            ps.setInt(2,nuevo.getVotante().getId());
            ps.setFloat(3,nuevo.getPuntaje());

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
            Toast.makeText(context, "Reseña registrada", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "No se puedo registrar la reseña", Toast.LENGTH_SHORT).show();
        }

    }
}
