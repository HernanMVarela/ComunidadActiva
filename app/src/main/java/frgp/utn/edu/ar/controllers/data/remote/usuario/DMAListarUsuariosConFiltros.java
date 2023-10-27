package frgp.utn.edu.ar.controllers.data.remote.usuario;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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

public class DMAListarUsuariosConFiltros extends AsyncTask<String, Void, String> {

    List<Usuario> listaUsuarios;
    ListView lvUsuarios;
    Context ct;
    String tipoUser = "";
    String textoBusq = "";
    String result = "";

    public DMAListarUsuariosConFiltros(Context ct, ListView lvUsuarios, String tipo, String texto){
        this.ct = ct;
        this.lvUsuarios = lvUsuarios;
        this.tipoUser = tipo;
        this.textoBusq = texto;
    }
    @Override
    protected String doInBackground(String... strings) {
        listaUsuarios = new ArrayList<Usuario>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String query = "SELECT " +
                    "U.ID AS UsuarioID, " +
                    "U.USERNAME AS UsuarioUsername, " +
                    "U.PASSWORD AS UsuarioPassword, " +
                    "U.PUNTUACION AS UsuarioPuntuacion, " +
                    "U.NOMBRE AS UsuarioNombre, " +
                    "U.APELLIDO AS UsuarioApellido, " +
                    "U.TELEFONO AS UsuarioTelefono, " +
                    "U.CORREO AS UsuarioCorreo, " +
                    "U.FECHA_NAC AS UsuarioFechaNac, " +
                    "U.CREACION AS UsuarioCreacion, " +
                    "U.ID_ESTADO AS UsuarioEstadoID, " +
                    "U.ID_TIPO AS UsuarioTipoID, " +
                    "U.COD_RECUPERACION AS UsuarioCodRecuperacion, " +
                    "U.FECHA_BLOQUEO AS UsuarioFechaBloqueo, " +
                    "EU.ID AS EstadoID, " +
                    "EU.ESTADO AS Estado, " +
                    "TU.ID AS TipoID, " +
                    "TU.TIPO AS Tipo " +
                    "FROM " +
                    "USUARIOS U " +
                    "INNER JOIN ESTADOS_USUARIO EU ON U.ID_ESTADO = EU.ID " +
                    "INNER JOIN TIPOS_USUARIO TU ON U.ID_TIPO = TU.ID " +
                    "WHERE " +
                    "(U.USERNAME LIKE ? OR U.NOMBRE LIKE ? OR U.APELLIDO LIKE ?) AND TU.TIPO LIKE ?";


            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, "%" + textoBusq + "%");
            preparedStatement.setString(2, "%" + textoBusq + "%");
            preparedStatement.setString(3, "%" + textoBusq + "%");
            preparedStatement.setString(4, tipoUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(resultSet.getInt("UsuarioID"));
                usuario.setUsername(resultSet.getString("UsuarioUsername"));
                usuario.setPassword(resultSet.getString("UsuarioPassword"));
                usuario.setPuntuacion(resultSet.getInt("UsuarioPuntuacion"));
                usuario.setNombre(resultSet.getString("UsuarioNombre"));
                usuario.setApellido(resultSet.getString("UsuarioApellido"));
                usuario.setTelefono(resultSet.getString("UsuarioTelefono"));
                usuario.setCorreo(resultSet.getString("UsuarioCorreo"));
                usuario.setFecha_nac(resultSet.getDate("UsuarioFechaNac"));
                usuario.setFecha_alta(resultSet.getDate("UsuarioCreacion"));
                usuario.setEstado(new EstadoUsuario(resultSet.getInt("UsuarioEstadoID"), resultSet.getString("Estado")));
                usuario.setTipo(new TipoUsuario(resultSet.getInt("UsuarioTipoID"), resultSet.getString("Tipo")));
                usuario.setCodigo_recuperacion(resultSet.getString("UsuarioCodRecuperacion"));
                usuario.setFecha_bloqueo(resultSet.getDate("UsuarioFechaBloqueo"));
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
