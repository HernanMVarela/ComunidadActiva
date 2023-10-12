package frgp.utn.edu.ar.controllers.DAOImpl.Usuario;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAUpdateUsuario extends AsyncTask<String, Void, Boolean> {

    private Context context;
    private Usuario modificado;

    public DMAUpdateUsuario(Usuario nuevo, Context ct)
    {
        this.modificado = nuevo;
        context = ct;
    }

    @Override
    protected Boolean doInBackground(String... urls) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE usuarios SET username = ?, password = ?, puntuacion = ?, nombre = ?, apellido = ?, telefono = ?, correo = ?, fecha_nac = ?, fecha_alta = ?, estado = ?, tipo = ? WHERE id = ?");

            preparedStatement.setString(1, modificado.getUsername());
            preparedStatement.setString(2, modificado.getPassword());
            preparedStatement.setInt(3, modificado.getPuntuacion());
            preparedStatement.setString(4, modificado.getNombre());
            preparedStatement.setString(5, modificado.getApellido());
            preparedStatement.setString(6, modificado.getTelefono());
            preparedStatement.setString(7, modificado.getCorreo());
            preparedStatement.setDate(8, (Date) modificado.getFecha_nac());
            preparedStatement.setDate(9, (Date) modificado.getFecha_alta());
            preparedStatement.setInt(10, modificado.getEstado().getId());
            preparedStatement.setInt(11, modificado.getTipo().getId());
            preparedStatement.setInt(12, modificado.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();
            if (rowsAffected > 0) {
                Log.i("Estado","Modificado");
                return true;
            } else {
                Log.e("Estado","NO Modificado");
                return false;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e("Error", e.getMessage());
            return false;
        }
    }
}
