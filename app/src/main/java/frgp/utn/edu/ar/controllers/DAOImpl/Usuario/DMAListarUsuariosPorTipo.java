package frgp.utn.edu.ar.controllers.DAOImpl.Usuario;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.DAOImpl.Usuario.EstadoUsuario.DMABuscarEstadoUsuarioPorId;
import frgp.utn.edu.ar.controllers.DAOImpl.Usuario.TipoUsuario.DMABuscarTipoUsuarioPorId;

public class DMAListarUsuariosPorTipo extends AsyncTask<String, Void, List<Usuario>> {

    private final Context context;
    private int idTipoUsuario;

    public DMAListarUsuariosPorTipo(Context ct, int idTipoUsuario)
    {
        context = ct;
        this.idTipoUsuario = idTipoUsuario;
    }

    @Override
    protected List<Usuario> doInBackground(String... urls) {
        List<Usuario> listaUsuarios = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM usuarios WHERE id_tipo = ?");
            preparedStatement.setInt(1, idTipoUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Usuario usuario = new Usuario();
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

                listaUsuarios.add(usuario);
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
