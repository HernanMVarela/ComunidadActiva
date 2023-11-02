package frgp.utn.edu.ar.controllers.data.remote.log;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.Logs;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;

public class DMAListarLogsPorUsuario extends AsyncTask<String, Void, List<Logs>> {

    private int userId;

    public DMAListarLogsPorUsuario(int userId) {
        this.userId = userId;
    }

    @Override
    protected List<Logs> doInBackground(String... urls) {

        List<Logs> logs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            ///TImeStamp 10 days ago
            Timestamp timestamp = new Timestamp(System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000);
            System.out.println(timestamp);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM LOGS " +
                                                                           "WHERE ID_USER = ? " +
                                                                           "AND FECHA > ?" +
                                                                           "ORDER BY FECHA DESC;");
            preparedStatement.setInt(1, userId);
            preparedStatement.setTimestamp(2, timestamp);
            ResultSet rs = preparedStatement.executeQuery();
            logs = new ArrayList<>();
            while (rs.next()) {
                Logs log = new Logs();
                log.setID(rs.getInt("ID"));
                log.setIdUser(rs.getInt("ID_USER"));
                log.setAccion(LogsEnum.valueOf(rs.getString("ACCION")));
                log.setDescripcion(rs.getString("DESCRIPCION"));
                log.setFecha(rs.getTimestamp("FECHA"));
                logs.add(log);
            }
            preparedStatement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logs;
    }
}
