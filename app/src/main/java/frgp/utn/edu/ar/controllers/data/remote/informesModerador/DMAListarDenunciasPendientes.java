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

public class DMAListarDenunciasPendientes extends AsyncTask<String, Void, JSONArray> {

    Date fechaInicio;
    Date fechaFin;

    public DMAListarDenunciasPendientes(Date fechaInicio, Date fechaFin) {
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
                            "(SELECT DP.TITULO, U.USERNAME, ED.ESTADO, DP.FECHA_CREACION "+
                            "FROM DENUNCIAS_PROYECTOS DP "+
                            "JOIN USUARIOS U ON DP.ID_USER = U.ID "+
                            "JOIN ESTADOS_DENUNCIA ED ON DP.ID_ESTADO = ED.ID "+
                            "WHERE ED.ESTADO IN ('CERRADA', 'CANCELADA') "+
                            "AND DP.FECHA_CREACION BETWEEN ? AND ? " +
                            "UNION ALL "+
                            "SELECT DR.TITULO, U.USERNAME, ED.ESTADO, DR.FECHA_CREACION "+
                            "FROM DENUNCIAS_REPORTES DR "+
                            "JOIN USUARIOS U ON DR.ID_USER = U.ID "+
                            "JOIN ESTADOS_DENUNCIA ED ON DR.ID_ESTADO = ED.ID "+
                            "WHERE ED.ESTADO IN ('CERRADA', 'CANCELADA') "+
                            "AND DR.FECHA_CREACION BETWEEN ? AND ?) " +
                            "AS DenunciasCombinadas "+
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
