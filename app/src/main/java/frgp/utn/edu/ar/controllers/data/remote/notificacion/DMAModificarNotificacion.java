package frgp.utn.edu.ar.controllers.data.remote.notificacion;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.model.Notificacion;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAModificarNotificacion extends AsyncTask<String, Void, Boolean> {

    private Notificacion notificacion;

    public DMAModificarNotificacion(Notificacion notificacion) {
        this.notificacion = notificacion;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE NOTIFICACIONES SET LECTURA = ? WHERE id = ?");
            preparedStatement.setBoolean(1, notificacion.getLectura());
            preparedStatement.setInt(2, notificacion.getID());

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
