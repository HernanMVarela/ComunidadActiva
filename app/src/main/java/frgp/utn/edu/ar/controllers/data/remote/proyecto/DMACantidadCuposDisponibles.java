package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMACantidadCuposDisponibles extends AsyncTask<String, Void, Integer> {

    private int idProyecto, cupos;

    public DMACantidadCuposDisponibles(int cupos, int idProyecto)
    {
        this.cupos=cupos;
        this.idProyecto=idProyecto;
    }

    @Override
    protected Integer doInBackground(String... urls) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String query = "SELECT count(*) as cuposOcupados FROM USERS_PROYECTO WHERE ID_PROYECTO = ? AND FECHA_SALIDA IS NULL";

            PreparedStatement preparedStatement = con.prepareStatement(query);

            preparedStatement.setInt(1,idProyecto);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int ocupados = rs.getInt("cuposOcupados");
                preparedStatement.close();
                con.close();
                return cupos - ocupados;
            }else{
                preparedStatement.close();
                con.close();
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}