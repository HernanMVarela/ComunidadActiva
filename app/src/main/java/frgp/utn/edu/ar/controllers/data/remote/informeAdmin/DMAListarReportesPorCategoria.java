package frgp.utn.edu.ar.controllers.data.remote.informeAdmin;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAListarReportesPorCategoria extends AsyncTask<String, Void, JSONArray> {

    private Date fechaInicio;
    private Date fechaFin;
    public DMAListarReportesPorCategoria(Date fechaInicio, Date fechaFin) {
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
            PreparedStatement preparedStatement = con.prepareStatement("SELECT TR.TIPO AS TIPO_REPORTE, COUNT(R.ID) AS CANTIDAD " +
                                                                           "FROM REPORTES R INNER JOIN TIPOS_REPORTE TR ON R.ID_TIPO = TR.ID " +
                                                                           "WHERE R.FECHA BETWEEN ? AND ? " +
                                                                           "GROUP BY TIPO_REPORTE;");
            preparedStatement.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(fechaFin.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                entry = new JSONObject();
                entry.put("tipo_reporte", resultSet.getString("TIPO_REPORTE"));
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
