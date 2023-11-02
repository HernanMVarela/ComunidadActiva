package frgp.utn.edu.ar.controllers.data.remote.notificacion;

import android.os.AsyncTask;
import android.util.Log;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.model.Notificacion;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMANuevaNotificacion extends AsyncTask<String, Void, Boolean> {

    private Notificacion nuevo;

    public DMANuevaNotificacion(Notificacion nuevo)
    {
        this.nuevo = nuevo;
    }

    @Override
    protected Boolean doInBackground(String... urls) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO NOTIFICACIONES (id_user, descripcion, fecha, lectura) VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, nuevo.getIdUser());
            preparedStatement.setString(2, nuevo.getDescripcion());
            preparedStatement.setTimestamp(3, new java.sql.Timestamp(nuevo.getFecha().getTime()));
            preparedStatement.setBoolean(4, nuevo.getLectura());

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();
            if (rowsAffected > 0) {
                Log.i("Estado","Agregado");
                return true;
            } else {
                Log.e("Estado","NO Agregado");
                return false;
            }
        }
        catch (MySQLIntegrityConstraintViolationException s){
            Log.e("duplicado","Log duplicado");
            s.printStackTrace();
            return false;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
