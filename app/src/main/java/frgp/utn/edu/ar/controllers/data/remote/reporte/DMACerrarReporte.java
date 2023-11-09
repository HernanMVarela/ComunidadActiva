package frgp.utn.edu.ar.controllers.data.remote.reporte;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.model.CierreReporte;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMACerrarReporte extends AsyncTask<String, Void, Boolean> {

    CierreReporte cierreReporte;
    private int dataRowModif;

    //Constructor
    public DMACerrarReporte(CierreReporte cierreReporte)
    {
        this.cierreReporte = cierreReporte;
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String query = "UPDATE CIERRES_REPORTE SET ID_ESTADOCIERRE = ? WHERE ID_REPORTE=? AND ID_USER =?;";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1,cierreReporte.getEstado().getId());
            ps.setInt(2,cierreReporte.getReporte().getId());
            ps.setInt(3,cierreReporte.getUser().getId());

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
