package frgp.utn.edu.ar.DAOImpl.Proyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.DAOImpl.Connector.DataDB;
import frgp.utn.edu.ar.entidades.Proyecto;

public class DMAUpdateProyecto extends AsyncTask<String, Void, Boolean> {

    private Context context;
    private Proyecto modificado;

    public DMAUpdateProyecto(Proyecto nuevo, Context ct)
    {
        this.modificado = nuevo;
        context = ct;
    }

    @Override
    protected Boolean doInBackground(String... urls) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE proyectos SET titulo = ?, descripcion = ?, latitud = ?, longitud = ?, fecha = ?, cupo = ?, id_user = ?, , id_tipo = ?, id_estado = ?  WHERE id = ?");

            preparedStatement.setString(1, modificado.getTitulo());
            preparedStatement.setString(2, modificado.getDescripcion());
            preparedStatement.setString(3, String.valueOf(modificado.getLatitud()));
            preparedStatement.setString(4, String.valueOf(modificado.getLongitud()));
            preparedStatement.setDate(5, (Date) modificado.getFecha());
            preparedStatement.setInt(6, modificado.getCupo());
            preparedStatement.setInt(7, modificado.getOwner().getId());
            preparedStatement.setInt(8, modificado.getTipo().getId());
            preparedStatement.setInt(9, modificado.getEstado().getId());

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
