package frgp.utn.edu.ar.controllers.data.remote.informesModerador;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAListarPublicacionesEliminadas extends AsyncTask<String, Void, JSONArray> {

    Date fechaInicio;
    Date fechaFin;

    public DMAListarPublicacionesEliminadas(Date fechaInicio, Date fechaFin) {
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
            PreparedStatement preparedStatement = con.prepareStatement(
                    "SELECT TITULO, USERNAME, ESTADO, Fecha_Creacion "+
                            "FROM " +
                                "(SELECT P.TITULO, U.USERNAME, ED.ESTADO, P.FECHA_CREACION "+
                                "FROM PROYECTOS P "+
                                "JOIN USUARIOS U ON P.ID_USER = U.ID "+
                                "JOIN ESTADOS_PROYECTO ED ON P.ID_ESTADO = ED.ID "+
                                "WHERE P.ID_ESTADO = 6 "+
                                "AND DP.FECHA_CREACION BETWEEN ? AND ? " +
                                "UNION ALL "+
                                "SELECT R.TITULO, U.USERNAME, ED.ESTADO, R.FECHA_CREACION "+
                                "FROM REPORTES R "+
                                "JOIN USUARIOS U ON R.ID_USER = U.ID "+
                                "JOIN ESTADOS_REPORTE ED ON R.ID_ESTADO = ED.ID "+
                                "WHERE R.ID_ESTADO = 7 "+
                                "AND DR.FECHA_CREACION BETWEEN ? AND ?) " +
                            "AS ResultadoCombinado "+
                            "GROUP BY TITULO, USERNAME, ESTADO, Fecha_Creacion;");

            preparedStatement.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(fechaFin.getTime()));
            preparedStatement.setDate(3, new java.sql.Date(fechaInicio.getTime()));
            preparedStatement.setDate(4, new java.sql.Date(fechaFin.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                entry = new JSONObject();
                entry.put("TITULO", resultSet.getString("TITULO"));
                entry.put("USERNAME", resultSet.getString("USERNAME"));
                entry.put("ESTADO", resultSet.getString("ESTADO"));

                response.put(entry);
            }
            preparedStatement.close();
            con.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
