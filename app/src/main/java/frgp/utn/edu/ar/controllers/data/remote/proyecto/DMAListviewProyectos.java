package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.content.Context;
import android.os.AsyncTask;
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
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.model.TipoProyecto;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.ui.adapters.ListaProyectosAdapter;

public class DMAListviewProyectos extends AsyncTask<String, Void, String> {
    private Context context;
    private ListView listado;
    private static String resultado;
    private static List<Proyecto> listaDeProyectos;
    private int IdTipoProyecto = 1;
    private int IdEstadoProyecto= 1;
    public DMAListviewProyectos(ListView listview, Context ct, int idTipoProyecto, int idEstadoProyecto) {
        listado = listview;
        context = ct;
        IdTipoProyecto = idTipoProyecto;
        IdEstadoProyecto = idEstadoProyecto;
    }
    @Override
    protected String doInBackground(String... urls) {
        try {
            listaDeProyectos = new ArrayList<Proyecto>();
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            String query = "SELECT P.ID, P.TITULO, P.DESCRIPCION, P.LATITUD, P.LONGITUD, P.CUPO, P.ID_USER, P.ID_TIPO, P.ID_ESTADO, P.CONTACTO, P.AYUDA_ESPECIFICA, " +
                    "U.USERNAME, TP.TIPO, EP.ESTADO FROM PROYECTOS AS P " +
                    "INNER JOIN USUARIOS AS U ON P.ID_USER = U.ID " +
                    "INNER JOIN TIPOS_PROYECTO AS TP ON P.ID_TIPO = TP.ID " +
                    "INNER JOIN ESTADOS_PROYECTO AS EP ON P.ID_ESTADO = EP.ID " +
                    "WHERE P.ID_TIPO = ? AND P.ID_ESTADO = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, IdTipoProyecto);
            preparedStatement.setInt(2, IdEstadoProyecto);
            ResultSet rs = preparedStatement.executeQuery();
            resultado = " ";
            while (rs.next()) {
                TipoProyecto tipo = new TipoProyecto(rs.getInt("P.ID_TIPO"), rs.getString("TP.TIPO"));
                EstadoProyecto estado = new EstadoProyecto(rs.getInt("P.ID_ESTADO"), rs.getString("EP.ESTADO"));
                Usuario usu = new Usuario();usu.setId(rs.getInt("P.ID_USER"));usu.setUsername(rs.getString("U.USERNAME"));

                Proyecto proyectobuscado = new Proyecto();
                proyectobuscado.setId(rs.getInt("P.ID"));
                proyectobuscado.setTitulo(rs.getString("P.TITULO"));
                proyectobuscado.setDescripcion(rs.getString("P.DESCRIPCION"));
                proyectobuscado.setLatitud(rs.getDouble("P.LATITUD"));
                proyectobuscado.setLongitud(rs.getDouble("P.LONGITUD"));
                proyectobuscado.setCupo(rs.getInt("P.CUPO"));
                proyectobuscado.setOwner(usu);
                proyectobuscado.setTipo(tipo);
                proyectobuscado.setEstado(estado);
                proyectobuscado.setContacto(rs.getString("P.CONTACTO"));
                proyectobuscado.setRequerimientos(rs.getString("P.AYUDA_ESPECIFICA"));

                listaDeProyectos.add(proyectobuscado);
            }
            resultado = "Conexion exitosa";
        } catch (Exception e) {
            e.printStackTrace();
            resultado = "Conexion no exitosa";
        }
        return resultado;
    }
    protected void onPostExecute(String response) {
        try{
        if(listaDeProyectos.isEmpty()) {
            Toast.makeText(context, "Nulo", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
                }
        else {
            ListaProyectosAdapter adapter = new ListaProyectosAdapter(context, listaDeProyectos);
            listado.setAdapter(adapter);
            Toast.makeText(context, "Carga Exitosa", Toast.LENGTH_SHORT).show();
        }
             }
        catch (Error e){
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        }
    }
}
