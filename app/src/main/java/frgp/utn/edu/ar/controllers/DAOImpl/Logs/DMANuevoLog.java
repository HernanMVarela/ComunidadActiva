package frgp.utn.edu.ar.controllers.DAOImpl.Logs;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.model.Logs;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMANuevoLog extends AsyncTask<String, Void, Boolean> {

    private final Context context;
    private Logs nuevo;

    public DMANuevoLog(Logs nuevo, Context ct)
    {
        this.nuevo = nuevo;
        context = ct;
    }

    @Override
    protected Boolean doInBackground(String... urls) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO logs (id_usuario, accion, descripcion, fecha) VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, nuevo.getIdUser());
            preparedStatement.setString(2, nuevo.getAccion().name());
            preparedStatement.setString(3, nuevo.getDescripcion());
            preparedStatement.setDate(4, (Date) nuevo.getFecha());

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
            Log.e("duplicado","usuario duplicado");
            s.printStackTrace();
            return false;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
