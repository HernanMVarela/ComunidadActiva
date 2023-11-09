package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAUsuariosDelProyecto extends AsyncTask<String, Void, String> {
    private Context context;
    private ListView listado;
    private static String resultado;
    private static List<Usuario> listaDeUsuarios;
    private int idProyecto = 1;
    public DMAUsuariosDelProyecto(List<Usuario> lista,int idProyectoB, Context ct) {
        listaDeUsuarios = lista;
        context = ct;
        idProyecto = idProyectoB;
    }
    @Override
    protected String doInBackground(String... urls) {
        listaDeUsuarios = new ArrayList<Usuario>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String query = "SELECT P.ID_USER, P.ID_PROYECTO, U.ID, U.USERNAME, FROM USERS_PROYECTO AS P " +
                    "INNER JOIN USUARIOS AS U ON P.ID_USER = U.ID WHERE P.ID_PROYECTO = ? AND P.FECHA_SALIDA IS NULL";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1,idProyecto);
            ResultSet rs = preparedStatement.executeQuery();
            resultado = " ";
            while (rs.next()) {
                Usuario usuariobuscado = new Usuario();
                usuariobuscado.setId(rs.getInt("P.ID_USER"));
                usuariobuscado.setUsername(rs.getString("P.U.Username"));
                listaDeUsuarios.add(usuariobuscado);
            }
            rs.close();
            con.close();
            resultado = "Conexion exitosa";
        } catch (Exception e) {
            e.printStackTrace();
            resultado = "Conexion no exitosa";
        }
        return resultado;
    }
    protected void onPostExecute(String response) {
        try{
        if(listaDeUsuarios.isEmpty()) {
            resultado = "Sin Resultados";
                }
        else {
            resultado = "Resultados";
        }
             }
        catch (Error e){
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        }
    }
}
