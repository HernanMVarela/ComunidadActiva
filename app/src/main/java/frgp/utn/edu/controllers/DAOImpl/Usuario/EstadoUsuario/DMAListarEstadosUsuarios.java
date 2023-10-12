package frgp.utn.edu.controllers.DAOImpl.Usuario.EstadoUsuario;

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
import frgp.utn.edu.controllers.data.model.EstadoUsuario;

public class DMAListarEstadosUsuarios extends AsyncTask<String, Void, List<EstadoUsuario>> {

    private final Context context;

    public DMAListarEstadosUsuarios(Context ct)
    {
        context = ct;
    }

    @Override
    protected List<EstadoUsuario> doInBackground(String... urls) {
        List<EstadoUsuario> listaUsuarios = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM estados_usuario");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                EstadoUsuario tipoUsuario = new EstadoUsuario();
                tipoUsuario.setId(resultSet.getInt("id"));
                tipoUsuario.setEstado(resultSet.getString("estado"));
                listaUsuarios.add(tipoUsuario);
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
        return listaUsuarios;
    }
}
