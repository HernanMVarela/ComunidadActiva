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

public class DMAListarDenucniasCerradas extends AsyncTask<String, Void, JSONArray> {

    Date fechaInicio;
    Date fechaFin;

    public DMAListarDenucniasCerradas(Date fechaInicio, Date fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    @Override
    protected JSONArray doInBackground(String... strings) {

        JSONArray response = new JSONArray();
        JSONObject entry = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement(
                    "SELECT TITULO, USERNAME, ESTADO, Fecha_Creacion, SUM(1) AS TotalCoincidencias ,"+
                            "(SELECT SUM(1) FROM ( SELECT 1 FROM DENUNCIAS_PROYECTOS DP WHERE DP.ID_ESTADO IN (3, 4) "+
                            "UNION ALL"+
                            "SELECT 1 FROM DENUNCIAS_REPORTES DR WHERE DR.ID_ESTADO IN (3, 4)) AS TodasLasDenuncias) AS TotalDenuncias"+
                            "FROM ("+
                            "SELECT DP.TITULO, U.USERNAME, ED.ESTADO, DP.FECHA_CREACION"+
                            "FROM DENUNCIAS_PROYECTOS DP"+
                            "JOIN USUARIOS U ON DP.ID_USER = U.ID"+
                            "JOIN ESTADOS_DENUNCIA ED ON DP.ID_ESTADO = ED.ID"+
                            "WHERE ED.ESTADO IN ('Cerrada', 'Cancelada')"+
                            "UNION ALL"+
                            "SELECT DR.TITULO, U.USERNAME, ED.ESTADO, DR.FECHA_CREACION"+
                            "FROM DENUNCIAS_REPORTES DR"+
                            "JOIN USUARIOS U ON DR.ID_USER = U.ID"+
                            "JOIN ESTADOS_DENUNCIA ED ON DR.ID_ESTADO = ED.ID"+
                            "WHERE ED.ESTADO IN ('Cerrada', 'Cancelada')"+
                            ") AS DenunciasCombinadas"+
                            "GROUP BY TITULO, USERNAME, ESTADO, Fecha_Creacion;");
            preparedStatement.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(fechaFin.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                entry = new JSONObject();
                entry.put("TITULO", resultSet.getString("TITULO"));
                entry.put("NOMBRE", resultSet.getInt("USERNAME"));
                entry.put("ESTADO", resultSet.getInt("ESTADO"));
                entry.put("Fecha_Creacion", resultSet.getInt("Fecha_Creacion"));
                entry.put("TotalDenuncias", resultSet.getInt("TotalDenuncias"));
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
