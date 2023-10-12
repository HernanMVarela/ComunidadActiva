package frgp.utn.edu.ar.controllers.DAOImpl.Usuario.TipoUsuario;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAListarTiposUsuarios extends AsyncTask<String, Void, List<TipoUsuario>> {

    private final Context context;

    public DMAListarTiposUsuarios(Context ct)
    {
        context = ct;
    }

    @Override
    protected List<TipoUsuario> doInBackground(String... urls) {
        List<TipoUsuario> listaTiposUsuarios = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM tipos_usuario");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                TipoUsuario tipoUsuario = new TipoUsuario();
                tipoUsuario.setId(resultSet.getInt("id"));
                tipoUsuario.setTipo(resultSet.getString("tipo"));
                listaTiposUsuarios.add(tipoUsuario);
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
        return listaTiposUsuarios;
    }
}
