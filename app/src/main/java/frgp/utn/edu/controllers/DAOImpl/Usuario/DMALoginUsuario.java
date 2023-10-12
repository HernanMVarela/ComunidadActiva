package frgp.utn.edu.controllers.DAOImpl.Usuario;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import frgp.utn.edu.controllers.data.remote.DataDB;
import frgp.utn.edu.controllers.DAOImpl.Usuario.EstadoUsuario.DMABuscarEstadoUsuarioPorId;
import frgp.utn.edu.controllers.DAOImpl.Usuario.TipoUsuario.DMABuscarTipoUsuarioPorId;
import frgp.utn.edu.controllers.data.model.Usuario;

public class DMALoginUsuario extends AsyncTask<String, Void, Usuario> {

    private final Context context;
    private String username;
    private String password;
    //Constructor
    public DMALoginUsuario(String username, String password, Context ct)
    {
        this.username = username;
        this.password = password;
        context = ct;
    }

    @Override
    protected Usuario doInBackground(String... urls) {
        Usuario usuario = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM usuarios WHERE username = ? AND password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                usuario = new Usuario();
                usuario.setId(resultSet.getInt("id"));
                usuario.setUsername(resultSet.getString("username"));
                usuario.setPassword(resultSet.getString("password"));
                usuario.setPuntuacion(resultSet.getInt("puntuacion"));
                usuario.setNombre(resultSet.getString("nombre"));
                usuario.setApellido(resultSet.getString("apellido"));
                usuario.setTelefono(resultSet.getString("telefono"));
                usuario.setCorreo(resultSet.getString("correo"));
                usuario.setFecha_nac(resultSet.getDate("fecha_nac"));
                usuario.setFecha_alta(resultSet.getDate("fecha_alta"));
                usuario.setEstado(new DMABuscarEstadoUsuarioPorId(resultSet.getInt("idEstado"),context).doInBackground(String.valueOf(resultSet.getInt("idEstado"))));
                usuario.setTipo(new DMABuscarTipoUsuarioPorId(resultSet.getInt("idTipo"),context).doInBackground(String.valueOf(resultSet.getInt("idTipo"))));
                Log.i("Usuario",usuario.getNombre());
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
        return usuario;
    }
}
