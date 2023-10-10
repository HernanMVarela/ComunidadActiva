package frgp.utn.edu.ar.DAOImpl.Proyecto;

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
import frgp.utn.edu.ar.DAOImpl.Proyecto.EstadoProyecto.DMABuscarEstadoProyectoPorId;
import frgp.utn.edu.ar.DAOImpl.Proyecto.TipoProyecto.DMABuscarTipoProyectoPorId;
import frgp.utn.edu.ar.DAOImpl.Usuario.DMABuscarUsuarioPorId;
import frgp.utn.edu.ar.entidades.Proyecto;

public class DMAListarProyectosPorEstado extends AsyncTask<String, Void, List<Proyecto>> {

    private final Context context;
    private int IdEstadoProyecto;

    public DMAListarProyectosPorEstado(Context ct, int IdEstadoProyecto)
    {
        context = ct;
        IdEstadoProyecto = IdEstadoProyecto;
    }

    @Override
    protected List<Proyecto> doInBackground(String... urls) {
        List<Proyecto> listaProyectos = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM proyectos WHERE id_estado = ?");
            preparedStatement.setInt(1, IdEstadoProyecto);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
               Proyecto proyecto = new Proyecto();
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

                listaProyectos.add(proyecto);
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
        return listaProyectos;
    }
}
