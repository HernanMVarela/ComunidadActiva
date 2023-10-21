package frgp.utn.edu.ar.controllers.data.remote.usuario;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAModificarUsuario extends AsyncTask<String, Void, Boolean> {

    private Usuario usuario;

    public DMAModificarUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE USUARIOS SET username = ?, " +
                                                                                               "password = ?, " +
                                                                                               "puntuacion = ?, " +
                                                                                               "nombre = ?, " +
                                                                                               "apellido = ?, " +
                                                                                               "telefono = ?, " +
                                                                                               "correo = ?, " +
                                                                                               "fecha_nac = ?, " +
                                                                                               "creacion = ?, " +
                                                                                               "id_estado = ?, " +
                                                                                               "id_tipo = ?, " +
                                                                                               "cod_recuperacion = ?, " +
                                                                                               "fecha_bloqueo = ? " +
                                                                                            "WHERE id = ?");
            preparedStatement.setString(1, usuario.getUsername());
            preparedStatement.setString(2, usuario.getPassword());
            preparedStatement.setInt(3, usuario.getPuntuacion());
            preparedStatement.setString(4, usuario.getNombre());
            preparedStatement.setString(5, usuario.getApellido());
            preparedStatement.setString(6, usuario.getTelefono());
            preparedStatement.setString(7, usuario.getCorreo());
            preparedStatement.setDate(8, new java.sql.Date(usuario.getFecha_nac().getTime()));
            preparedStatement.setDate(9, new java.sql.Date(usuario.getFecha_alta().getTime()));
            preparedStatement.setInt(10, usuario.getEstado().getId());
            preparedStatement.setInt(11, usuario.getTipo().getId());
            preparedStatement.setString(12, usuario.getCodigo_recuperacion());
            preparedStatement.setDate(13, new java.sql.Date(usuario.getFecha_bloqueo().getTime()));
            preparedStatement.setInt(14, usuario.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                preparedStatement.close();
                con.close();
                return true;
            } else {
                preparedStatement.close();
                con.close();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
