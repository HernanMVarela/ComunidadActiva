package frgp.utn.edu.controllers.DAOImpl.Usuario.EstadoUsuario;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import frgp.utn.edu.controllers.data.remote.DataDB;
import frgp.utn.edu.controllers.data.model.EstadoUsuario;

public class DMABuscarEstadoUsuarioPorId extends AsyncTask<String, Void, EstadoUsuario> {

    private final Context context;
    private final int id;

    public DMABuscarEstadoUsuarioPorId(int id, Context context)
    {
        this.context = context;
        this.id = id;
    }

    @Override
    public EstadoUsuario doInBackground(String... urls) {
        EstadoUsuario estadoUsuario = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM estados_usuario WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                estadoUsuario = new EstadoUsuario();
                estadoUsuario.setId(resultSet.getInt("id"));
                estadoUsuario.setEstado(resultSet.getString("estado"));
                Log.i("EstadoUsuario",estadoUsuario.getEstado());
            }
            preparedStatement.close();
            con.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
        return estadoUsuario;
    }
}
