package frgp.utn.edu.ar.controllers.data.remote.informesAdmin;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAListarProyectosPorCategoria extends AsyncTask<String, Void, JSONArray> {

    Date fechaInicio;
    Date fechaFin;
    public DMAListarProyectosPorCategoria(Date fechaInicio, Date fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    @Override
    protected JSONArray doInBackground(String... urls) {

        JSONArray response = new JSONArray();
        JSONObject entry = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT TP.TIPO AS TIPO_PROYECTO, COUNT(P.ID) AS CANTIDAD " +
                                                                           "FROM PROYECTOS P INNER JOIN TIPOS_PROYECTOS TP ON P.ID_TIPO = TP.ID " +
                                                                           "WHERE P.FECHA BETWEEN ? AND ? " +
                                                                           "GROUP BY TIPO_PROYECTO;");
            preparedStatement.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(fechaFin.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                entry = new JSONObject();
                entry.put("tipo_proyecto", resultSet.getString("TIPO_PROYECTO"));
                entry.put("cantidad", resultSet.getInt("CANTIDAD"));
                response.put(entry);
            }
            preparedStatement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
