package frgp.utn.edu.ar.controllers.data.remote.reporte;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAActualizarEstadoReporte extends AsyncTask<String, Void, Boolean> {

    private Reporte modificar;

    //Constructor
    public DMAActualizarEstadoReporte(Reporte modificar)
    {
        this.modificar = modificar;
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            // Define la consulta SQL de actualización
            String query = "UPDATE REPORTES SET ID_ESTADO = ? WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, modificar.getEstado().getId());
            ps.setInt(2, modificar.getId());
            int dataRowModif = ps.executeUpdate();

            ps.close();
            con.close();
            if(dataRowModif !=0){
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
