package frgp.utn.edu.controllers.DAOImpl.Proyecto.EstadoProyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import frgp.utn.edu.controllers.data.remote.DataDB;
import frgp.utn.edu.controllers.data.model.EstadoProyecto;

public class DMABuscarEstadoProyectoPorId extends AsyncTask<String, Void, EstadoProyecto> {

    private final Context context;
    private final int id;

    public DMABuscarEstadoProyectoPorId(int id, Context context)
    {
        this.context = context;
        this.id = id;
    }

    @Override
    public EstadoProyecto doInBackground(String... urls) {
        EstadoProyecto estadoProyecto = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM estados_proyecto WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                estadoProyecto = new EstadoProyecto();
                estadoProyecto.setId(resultSet.getInt("id"));
                estadoProyecto.setEstado(resultSet.getString("estado"));
                Log.i("EstadoUsuario",estadoProyecto.getEstado());
            }
            preparedStatement.close();
            con.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
        return estadoProyecto;
    }
}
