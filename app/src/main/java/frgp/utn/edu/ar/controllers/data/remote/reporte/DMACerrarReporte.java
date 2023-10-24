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
    CierreReporte cierreReporte;
    private static String result2;
    private int dataRowModif;

    //Constructor
    public DMACerrarReporte(CierreReporte cierreReporte, Context ct)
    {
        this.cierreReporte = cierreReporte;
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

            ps.setInt(1,cierreReporte.getReporte().getId());
            ps.setInt(2,cierreReporte.getUser().getId());

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
            Reporte modifcar = cierreReporte.getReporte();
            modifcar.setEstado(new EstadoReporte(4,"CERRADO"));
            DMAActualizarEstadoReporte dmaActualizar = new DMAActualizarEstadoReporte(modifcar,context);
            dmaActualizar.execute();
            Toast.makeText(context, "El reporte ha sido cerrado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "No se pudo crear la solicitud", Toast.LENGTH_SHORT).show();
        }
    }
}
