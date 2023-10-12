package frgp.utn.edu.ar.controllers.DAOImpl.Proyecto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import frgp.utn.edu.ar.controllers.DAOImpl.Proyecto.EstadoProyecto.DMABuscarEstadoProyectoPorId;
import frgp.utn.edu.ar.controllers.DAOImpl.Proyecto.TipoProyecto.DMABuscarTipoProyectoPorId;
import frgp.utn.edu.ar.controllers.DAOImpl.Usuario.DMABuscarUsuarioPorId;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMABuscarProyectoPorId extends AsyncTask<String, Void, Proyecto> {

    private final Context context;
    private int id;
    //Constructor
    public DMABuscarProyectoPorId(int id, Context ct)
    {
        this.id = id;
        context = ct;
    }

    @SuppressLint("WrongThread")
    @Override
    protected Proyecto doInBackground(String... urls) {
        Proyecto proyecto = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM proyectos WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                proyecto = new Proyecto();
                proyecto.setId(resultSet.getInt("id"));
                proyecto.setTitulo(resultSet.getString("titulo"));
                proyecto.setDescripcion(resultSet.getString("descripcion"));
                proyecto.setLatitud(resultSet.getDouble("latitud"));
                proyecto.setLongitud(resultSet.getDouble("longitud"));
                proyecto.setFecha(resultSet.getDate("fecha"));
                proyecto.setCupo(resultSet.getInt("cupo"));
                proyecto.setOwner(new DMABuscarUsuarioPorId(resultSet.getInt("idUsuario"),context).doInBackground(String.valueOf(resultSet.getInt("idUsuario"))));
                proyecto.setTipo(new DMABuscarTipoProyectoPorId(resultSet.getInt("idTipoProyecto"),context).doInBackground(String.valueOf(resultSet.getInt("idTipoProyecto"))));
                proyecto.setEstado(new DMABuscarEstadoProyectoPorId(resultSet.getInt("idEstadoProyecto"),context).doInBackground(String.valueOf(resultSet.getInt("idEstadoProyecto"))));
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
        return proyecto;
    }
}
