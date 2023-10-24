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
import frgp.utn.edu.ar.controllers.data.model.EstadoReporte;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMACerrarReporte extends AsyncTask<String, Void, String> {

    private Context context;
    int idUser;
    int idReporte;
    private static String result2;
    private int dataRowModif;

    //Constructor
    public DMACerrarReporte(int idReporte, int idUser, Context ct)
    {
        this.idUser = idUser;
        this.idReporte = idReporte;
        this.context = ct;
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String query = "UPDATE CIERRES_REPORTE SET ID_ESTADOCIERRE = 4 WHERE ID_REPORTE=? AND ID_USER =?;";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1,idReporte);
            ps.setInt(2,idUser);

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
            Reporte modifcar = new Reporte();
            modifcar.setId(idReporte);
            modifcar.setEstado(new EstadoReporte(4,"CERRADO"));
            DMAActualizarEstadoReporte dmaActualizar = new DMAActualizarEstadoReporte(modifcar,context);
            dmaActualizar.execute();
        }else{
            Toast.makeText(context, "No se pudo crear la solicitud", Toast.LENGTH_SHORT).show();
        }
    }
}
