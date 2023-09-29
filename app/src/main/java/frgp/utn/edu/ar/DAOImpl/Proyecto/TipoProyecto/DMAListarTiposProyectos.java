package frgp.utn.edu.ar.DAOImpl.Proyecto.TipoProyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

import frgp.utn.edu.ar.DAOImpl.Connector.DataDB;
import frgp.utn.edu.ar.entidades.TipoProyecto;
import frgp.utn.edu.ar.entidades.TipoUsuario;

public class DMAListarTiposProyectos extends AsyncTask<String, Void, List<TipoProyecto>> {

    private final Context context;

    public DMAListarTiposProyectos(Context ct)
    {
        context = ct;
    }

    @Override
    protected List<TipoProyecto> doInBackground(String... urls) {
        List<TipoProyecto> listaTiposProyectos = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM tipos_proyecto");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                TipoProyecto tipoProyecto = new TipoProyecto();
                tipoProyecto.setId(resultSet.getInt("id"));
                tipoProyecto.setTipo(resultSet.getString("tipo"));
                listaTiposProyectos.add(tipoProyecto);
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
        return listaTiposProyectos;
    }
}
