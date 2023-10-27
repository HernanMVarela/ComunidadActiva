package frgp.utn.edu.ar.controllers.data.remote.usuario;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.ui.adapters.ListaUsuariosAdapter;

public class DMAListarUsuariosCompleto extends AsyncTask<String, Void, String> {

    List<Usuario> listaUsuarios;
    ListView lvUsuarios;
    Context ct;
    String result = "";

    public DMAListarUsuariosCompleto(Context ct, ListView lvUsuarios){
        this.ct = ct;
        this.lvUsuarios = lvUsuarios;
    }
    @Override
    protected String doInBackground(String... strings) {
        listaUsuarios = new ArrayList<Usuario>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String query = "SELECT * FROM USUARIOS AS U INNER JOIN ESTADOS_USUARIO AS EU ON U.ID_ESTADO = EU.ID " +
                    "INNER JOIN TIPOS_USUARIO AS TU ON U.ID_TIPO = TU.ID ";

            PreparedStatement preparedStatement = con.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(resultSet.getInt("id"));
                usuario.setUsername(resultSet.getString("username"));
                usuario.setPassword(resultSet.getString("password"));
                usuario.setPuntuacion(resultSet.getInt("puntuacion"));
                usuario.setNombre(resultSet.getString("nombre"));
                usuario.setApellido(resultSet.getString("apellido"));
                usuario.setTelefono(resultSet.getString("telefono"));
                usuario.setCorreo(resultSet.getString("correo"));
                usuario.setFecha_nac(resultSet.getDate("fecha_nac"));
                usuario.setFecha_alta(resultSet.getDate("creacion"));
                usuario.setEstado(new EstadoUsuario(resultSet.getInt("id_estado"), resultSet.getString("estado")));
                usuario.setTipo(new TipoUsuario(resultSet.getInt("id_tipo"), resultSet.getString("tipo")));
                usuario.setCodigo_recuperacion(resultSet.getString("cod_recuperacion"));
                usuario.setFecha_bloqueo(resultSet.getDate("fecha_bloqueo"));

                listaUsuarios.add(usuario);
            }
            preparedStatement.close();
            con.close();
            return result;
        }catch (Exception e){
            e.printStackTrace();
            result = "ERROR";
            return result;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        ListaUsuariosAdapter adapter = new ListaUsuariosAdapter(ct,listaUsuarios);
        lvUsuarios.setAdapter(adapter);

    }
}
