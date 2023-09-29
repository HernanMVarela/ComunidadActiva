package frgp.utn.edu.ar.DAOImpl.Proyecto.TipoProyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import frgp.utn.edu.ar.DAOImpl.Connector.DataDB;
import frgp.utn.edu.ar.entidades.TipoProyecto;

public class DMABuscarTipoProyectoPorId extends AsyncTask<String, Void, TipoProyecto> {

    private final Context context;
    private final int id;

    public DMABuscarTipoProyectoPorId(int id, Context context)
    {
        this.context = context;
        this.id = id;
    }

    @Override
    public TipoProyecto doInBackground(String... urls) {
        TipoProyecto tipoProyecto = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM tipos_proyecto WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                tipoProyecto = new TipoProyecto();
                tipoProyecto.setId(resultSet.getInt("id"));
                tipoProyecto.setTipo(resultSet.getString("tipo"));
                Log.i("TipoUsuario",tipoProyecto.getTipo());
            }
            preparedStatement.close();
            con.close();
        }
            catch(Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return null;
        }
        return tipoProyecto;
    }
}
