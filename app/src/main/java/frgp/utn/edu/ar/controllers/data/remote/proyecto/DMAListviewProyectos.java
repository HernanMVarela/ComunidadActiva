package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.EstadoProyecto;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.model.TipoProyecto;
import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.ui.adapters.ListaProyectosAdapter;

public class DMAListviewProyectos extends AsyncTask<String, Void, String> {
    private Context context;
    private ListView listado;
    private static String result2;
    private static List<Proyecto> listaDeProyectos;
    private int IdTipoProyecto = -1;
    private int IdEstadoProyecto= -1;
    public DMAListviewProyectos(ListView listview, Context ct, int idTipoProyecto, int idEstadoProyecto) {
        listado = listview;
        context = ct;
        IdTipoProyecto = idTipoProyecto;
        IdEstadoProyecto = idEstadoProyecto;
    }
    @Override
    protected String doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();

            String query = "SELECT " +
                    "P.ID AS PID, " +
                    "P.TITULO AS PTitulo, " +
                    "P.DESCRIPCION AS PDesc, " +
                    "P.ID_TIPO AS IDTipoProyecto, " +
                    "P.ID_ESTADO AS IDEstadoProyecto, " +
                    "P.ID_USER AS UID, " +
                    "P.LATITUD AS PLatitud, " +
                    "P.LONGITUD AS PLongitud, " +
                    "P.CUPO AS PCupo, " +
                    "U.USERNAME AS Username, " +
                    "U.TELEFONO AS Telefono, " +
                    "U.CORREO AS Correo, " +
                    "TP.TIPO AS TipoProyecto, " +
                    "EP.ESTADO AS EstadoProyecto " +
                    "FROM PROYECTOS AS P " +
                    "WHERE id_tipo = ? AND id_estado = ? " +
                    "INNER JOIN USUARIOS AS U ON P.ID_USER = U.ID " +
                    "INNER JOIN TIPOS_PROYECTO AS TP ON P.ID_TIPO = TP.ID " +
                    "INNER JOIN ESTADOS_PROYECTO AS EP ON P.ID_ESTADO = EP.ID";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, IdTipoProyecto);
            preparedStatement.setInt(2, IdEstadoProyecto);
            ResultSet rs = preparedStatement.executeQuery();
            result2 = " ";
            listaDeProyectos = new ArrayList<Proyecto>();
            while (rs.next()) {
                Proyecto proyectobuscado = new Proyecto();
                Usuario user = new Usuario();
                user.setTipo(new TipoUsuario());
                user.setEstado(new EstadoUsuario());
                TipoProyecto tipo = new TipoProyecto(rs.getInt("IDTipoProyecto"), rs.getString("TipoProyecto"));
                EstadoProyecto estado = new EstadoProyecto(rs.getInt("IDEstadoProyecto"), rs.getString("EstadoProyecto"));
                user.setId(rs.getInt("UID"));
                user.setUsername(rs.getString("Username"));
                user.setTelefono(rs.getString("Telefono"));
                user.setCorreo(rs.getString("Correo"));
                proyectobuscado.setId(rs.getInt("PID"));
                proyectobuscado.setTitulo(rs.getString("PTitulo"));
                proyectobuscado.setDescripcion(rs.getString("PDesc"));
                proyectobuscado.setTipo(tipo);
                proyectobuscado.setEstado(estado);
                proyectobuscado.setCupo(rs.getInt("PCupo"));
                proyectobuscado.setOwner(user);
                proyectobuscado.setTipo(tipo);
                proyectobuscado.setEstado(estado);
                proyectobuscado.setLatitud(rs.getDouble("PLatitud"));
                proyectobuscado.setLongitud(rs.getDouble("PLongitud"));
                proyectobuscado.setOwner(user);
                listaDeProyectos.add(proyectobuscado);
            }
            response = "Conexion exitosa";
        } catch (Exception e) {
            e.printStackTrace();
            result2 = "Conexion no exitosa";
        }
        return response;
    }
    protected void onPostExecute(String response) {
        ListaProyectosAdapter adapter = new ListaProyectosAdapter(context, listaDeProyectos);
        if(listaDeProyectos!=null)
        {listado.setAdapter(adapter);}
    }
}
