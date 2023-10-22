package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

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
    public DMAListviewProyectos(ListView listview, Context ct) {
        listado = listview;
        context = ct;
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
                    "EP.ESTADO AS EstadoProyecto, " +
                    "6371 * 2 * ASIN(SQRT(" +
                    "POW(SIN(RADIANS(P.LATITUD - ?) / 2), 2) + " +
                    "COS(RADIANS(?)) * COS(RADIANS(P.LATITUD)) * " +
                    "POW(SIN(RADIANS(P.LONGITUD - ?) / 2), 2)" +
                    ")) AS Distancia " +
                    "FROM PROYECTOS AS P " +
                    "INNER JOIN USUARIOS AS U ON P.ID_USER = U.ID " +
                    "INNER JOIN TIPOS_PROYECTO AS TR ON P.ID_TIPO = TP.ID " +
                    "INNER JOIN ESTADOS_PROYECTO AS ER ON P.ID_ESTADO = EP.ID" +
                    "HAVING Distancia <= 5 " +
                    "ORDER BY Distancia;";

            PreparedStatement preparedStatement = con.prepareStatement(query);
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
