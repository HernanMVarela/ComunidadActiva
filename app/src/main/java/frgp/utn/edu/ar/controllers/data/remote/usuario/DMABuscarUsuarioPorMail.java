package frgp.utn.edu.ar.controllers.data.remote.usuario;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMABuscarUsuarioPorMail extends AsyncTask<String, Void, Usuario> {

    private String correo;
    //Constructor
    public DMABuscarUsuarioPorMail(String username)
    {
        this.correo = username;
    }

    @Override
    public Usuario doInBackground(String... urls) {
        Usuario usuario = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM USUARIOS AS U" +
                                                                           " INNER JOIN ESTADOS_USUARIO AS EU ON U.ID_ESTADO = EU.ID" +
                                                                           " INNER JOIN TIPOS_USUARIO AS TU ON U.ID_TIPO = TU.ID" +
                                                                           " WHERE correo = ?");
            preparedStatement.setString(1, correo);
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
                usuario.setFecha_alta(resultSet.getDate("creacion"));
                usuario.setEstado(new EstadoUsuario(resultSet.getInt("id_estado"), resultSet.getString("estado")));
                usuario.setTipo(new TipoUsuario(resultSet.getInt("id_tipo"), resultSet.getString("tipo")));
                usuario.setCodigo_recuperacion(resultSet.getString("cod_recuperacion"));
                usuario.setFecha_bloqueo(resultSet.getDate("fecha_bloqueo"));
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
