package frgp.utn.edu.ar.controllers.DAOImpl.Proyecto.EstadoProyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

import frgp.utn.edu.ar.controllers.data.model.EstadoProyecto;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAListarEstadosProyectos extends AsyncTask<String, Void, List<EstadoProyecto>> {

    private final Context context;

    public DMAListarEstadosProyectos(Context ct)
    {
        context = ct;
    }

    @Override
    protected List<EstadoProyecto> doInBackground(String... urls) {
        List<EstadoProyecto> listaProyectos = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM estados_proyecto");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                EstadoProyecto estadoProyecto = new EstadoProyecto();
                estadoProyecto.setId(resultSet.getInt("id"));
                estadoProyecto.setEstado(resultSet.getString("estado"));
                listaProyectos.add(estadoProyecto);
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
        return listaProyectos;
    }
}
