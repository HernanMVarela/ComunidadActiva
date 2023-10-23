package frgp.utn.edu.ar.controllers.data.remote.reporte;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAActualizarVotosReporte extends AsyncTask<String, Void, String> {

    private Context context;
    private Reporte modificar;
    private static String result2;
    private int dataRowModif;

    //Constructor
    public DMAActualizarVotosReporte(Reporte modificar, Context ct)
    {
        this.modificar = modificar;
        this.context = ct;
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            // Define la consulta SQL de actualización
            String query = "UPDATE REPORTES SET PUNTAJE = ?, CANT_VOTOS = ? WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setFloat(1, modificar.getPuntaje());
            ps.setInt(2, modificar.getCant_votos());
            ps.setInt(3, modificar.getId());
            dataRowModif = ps.executeUpdate();

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            response = "ERROR";
        }
        return response;
    }
}
