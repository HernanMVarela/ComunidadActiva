package frgp.utn.edu.ar.controllers.DAOImpl.Usuario;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import frgp.utn.edu.ar.controllers.DAOImpl.Usuario.EstadoUsuario.DMABuscarEstadoUsuarioPorId;
import frgp.utn.edu.ar.controllers.DAOImpl.Usuario.TipoUsuario.DMABuscarTipoUsuarioPorId;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.data.model.Usuario;

public class DMABuscarUsuarioPorId extends AsyncTask<String, Void, Usuario> {

    private final Context context;
    private int id;
    //Constructor
    public DMABuscarUsuarioPorId(int id, Context ct)
    {
        this.id = id;
        context = ct;
    }

    @Override
    public Usuario doInBackground(String... urls) {
        Usuario usuario = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO usuarios (username, password, puntuacion, nombre, apellido, telefono, correo, fecha_nac, fecha_alta. estado, tipo) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setInt(1, id);
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
