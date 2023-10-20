package frgp.utn.edu.ar.controllers.data.remote.usuario;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMABuscarUsuarioPorMail extends AsyncTask<String, Void, Usuario> {

    private final Context context;
    private String correo;
    //Constructor
    public DMABuscarUsuarioPorMail(String username, Context ct)
    {
        this.correo = username;
        context = ct;
    }

    @Override
    public Usuario doInBackground(String... urls) {
        Usuario usuario = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM USUARIOS WHERE correo = ?");
            preparedStatement.setString(1, correo);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                usuario = new Usuario();
                usuario.setId(resultSet.getInt("id"));
            }
            preparedStatement.close();
            con.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
        }
        return usuario;
    }
}
