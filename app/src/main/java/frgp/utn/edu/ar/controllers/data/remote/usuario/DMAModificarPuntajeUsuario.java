package frgp.utn.edu.ar.controllers.data.remote.usuario;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAModificarPuntajeUsuario extends AsyncTask<String, Void, Boolean> {

    private Usuario usuario;

    public DMAModificarPuntajeUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE USUARIOS SET PUNTUACION = ? WHERE id = ?");

            preparedStatement.setInt(1, usuario.getPuntuacion());
            preparedStatement.setInt(2, usuario.getId());

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
