package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMACuposDisponibles extends AsyncTask<String, Void, Boolean> {

    private int idProyecto, cupos;
    private int filasafectadas;

    public DMACuposDisponibles(int cupos, int idProyecto)
    {
        this.cupos=cupos;
        this.idProyecto=idProyecto;
    }

    @Override
    protected Boolean doInBackground(String... urls) {

        try {
            filasafectadas = 0;
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
                return ocupados < cupos;
            }else{
                preparedStatement.close();
                con.close();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}