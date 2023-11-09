package frgp.utn.edu.ar.controllers.data.remote.notificacion;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.Notificacion;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAListarNotificacionesNoLeidasPorUsuario extends AsyncTask<String, Void, List<Notificacion>> {

    private int userId;

    public DMAListarNotificacionesNoLeidasPorUsuario(int userId) {
        this.userId = userId;
    }

    @Override
    protected List<Notificacion> doInBackground(String... urls) {

        List<Notificacion> notificaciones = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM NOTIFICACIONES " +
                                                                           "WHERE ID_USER = ? " +
                                                                           "AND LECTURA = FALSE " +
                                                                           "ORDER BY FECHA DESC");
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            notificaciones = new ArrayList<>();
            while (rs.next()) {
                Notificacion notificacion = new Notificacion();
                notificacion.setID(rs.getInt("ID"));
                notificacion.setIdUser(rs.getInt("ID_USER"));
                notificacion.setDescripcion(rs.getString("DESCRIPCION"));
                notificacion.setFecha(rs.getTimestamp("FECHA"));
                notificacion.setLectura(rs.getBoolean("LECTURA"));
                notificaciones.add(notificacion);
            }
            preparedStatement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notificaciones;
    }
}
