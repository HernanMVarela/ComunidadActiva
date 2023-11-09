package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;


public class DMAActualizarEstadoProyecto extends AsyncTask<String, Void, Boolean> {
    private Proyecto modificar;
    private int dataRowModif;

    public DMAActualizarEstadoProyecto(Proyecto modificar) {
        this.modificar = modificar;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            // Define la consulta SQL de actualización
            String query = "UPDATE PROYECTOS SET ID_ESTADO = ? WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, modificar.getEstado().getId());
            ps.setInt(2, modificar.getId());
            dataRowModif = ps.executeUpdate();

            ps.close();
            con.close();

            return dataRowModif != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
