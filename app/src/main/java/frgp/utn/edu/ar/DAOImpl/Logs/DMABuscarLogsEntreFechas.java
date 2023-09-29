package frgp.utn.edu.ar.DAOImpl.Logs;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import frgp.utn.edu.ar.DAOImpl.Connector.DataDB;
import frgp.utn.edu.ar.entidades.Logs;
import frgp.utn.edu.ar.utils.LogsEnum;

public class DMABuscarLogsEntreFechas extends AsyncTask<String, Void, List<Logs>> {

    private final Context context;
    private Date fechaDesde;
    private Date fechaHasta;
    //Constructor
    public DMABuscarLogsEntreFechas( Context ct, Date fechaDesde, Date fechaHasta)
    {
        context = ct;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
    }

    @Override
    protected List<Logs> doInBackground(String... urls) {
        List<Logs> listaLogs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM logs WHERE fecha BETWEEN ? AND ?");
            preparedStatement.setDate(1, (java.sql.Date) fechaDesde);
            preparedStatement.setDate(2, (java.sql.Date) fechaHasta);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Logs log = new Logs();
                log.setID(resultSet.getInt("id"));
                log.setIdUser(resultSet.getInt("id_usuario"));
                log.setAccion(LogsEnum.valueOf(resultSet.getString("accion")));
                log.setDescripcion(resultSet.getString("descripcion"));
                log.setFecha(resultSet.getDate("fecha"));

                listaLogs.add(log);
            }
            resultSet.close();
            preparedStatement.close();
            con.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
        return listaLogs;
    }
}
