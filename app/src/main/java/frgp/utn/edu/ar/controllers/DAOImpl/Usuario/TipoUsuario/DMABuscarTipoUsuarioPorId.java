package frgp.utn.edu.ar.controllers.DAOImpl.Usuario.TipoUsuario;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMABuscarTipoUsuarioPorId extends AsyncTask<String, Void, TipoUsuario> {

    private final Context context;
    private final int id;

    public DMABuscarTipoUsuarioPorId(int id, Context context)
    {
        this.context = context;
        this.id = id;
    }

    @Override
    public TipoUsuario doInBackground(String... urls) {
        TipoUsuario tipoUsuario = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM tipos_usuario WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                tipoUsuario = new TipoUsuario();
                tipoUsuario.setId(resultSet.getInt("id"));
                tipoUsuario.setTipo(resultSet.getString("tipo"));
                Log.i("TipoUsuario",tipoUsuario.getTipo());
            }
            preparedStatement.close();
            con.close();
        }
            catch(Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
        return tipoUsuario;
    }
}
