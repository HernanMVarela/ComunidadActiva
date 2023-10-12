package frgp.utn.edu.controllers.DAOImpl.Logs;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

import frgp.utn.edu.controllers.data.remote.DataDB;
import frgp.utn.edu.controllers.data.model.Logs;
import frgp.utn.edu.controllers.utils.LogsEnum;

public class DMABuscarLogsPorUsuarioPorAccion extends AsyncTask<String, Void, List<Logs>> {

    private final Context context;
    private int id;

    private String accion;
    //Constructor
    public DMABuscarLogsPorUsuarioPorAccion(int id, Context ct, LogsEnum accion)
    {
        this.id = id;
        context = ct;
        this.accion = accion.name();
    }

    @Override
    protected List<Logs> doInBackground(String... urls) {
        List<Logs> listaLogs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM logs WHERE idUsuario = ? AND accion = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, accion);
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
