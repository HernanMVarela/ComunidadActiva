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
            String query = "SELECT ID, TITULO, DESCRIPCION, LATITUD, LONGITUD, CUPO, ID_USER, ID_TIPO, ID_ESTADO, CONTACTO, AYUDA_ESPECIFICA FROM PROYECTOS " +
                    "WHERE id_tipo = ? AND id_estado = ?"
                    // + "INNER JOIN USUARIOS AS U ON P.ID_USER = U.ID "
                    // + "INNER JOIN TIPOS_PROYECTO AS TP ON P.ID_TIPO = TP.ID "
                    // + "INNER JOIN ESTADOS_PROYECTO AS EP ON P.ID_ESTADO = EP.ID"
                    ;
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, IdTipoProyecto);
            preparedStatement.setInt(2, IdEstadoProyecto);
            ResultSet rs = preparedStatement.executeQuery();
            resultado = " ";
            while (rs.next()) {
                Proyecto proyectobuscado = new Proyecto();
                proyectobuscado.setId(rs.getInt("ID"));
                proyectobuscado.setTitulo(rs.getString("TITULO"));
                proyectobuscado.setContacto(rs.getString("CONTACTO"));
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
