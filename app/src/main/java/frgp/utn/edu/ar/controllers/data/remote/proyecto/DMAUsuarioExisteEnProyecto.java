package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAUsuarioExisteEnProyecto extends AsyncTask<String, Void, Boolean> {

    private int idUserN, idProyectoN;
    private int filasafectadas;

    public DMAUsuarioExisteEnProyecto(int idUser, int idProyecto)
    {
        idUserN=idUser;
        idProyectoN=idProyecto;
    }

    @Override
    protected Boolean doInBackground(String... urls) {

        try {
            filasafectadas = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String query = "SELECT * FROM USERS_PROYECTO WHERE ID_USER = ? AND ID_PROYECTO = ?";

            PreparedStatement preparedStatement = con.prepareStatement(query);

            preparedStatement.setInt(1,idUserN);
            preparedStatement.setInt(2,idProyectoN);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                preparedStatement.close();
                con.close();
                return rs.getDate("FECHA_SALIDA") != null;
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