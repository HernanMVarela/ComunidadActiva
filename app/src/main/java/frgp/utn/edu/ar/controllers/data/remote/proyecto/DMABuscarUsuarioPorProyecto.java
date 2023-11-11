package frgp.utn.edu.ar.controllers.data.remote.proyecto;

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
import java.util.Objects;

import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Voluntario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.ui.adapters.ListaVoluntariosAdapter;

public class DMABuscarUsuarioPorProyecto extends AsyncTask<String, Void, String> {
    private ListView listado;
    private Context context;
    private int id_proyecto;
    private List<Voluntario> listaUsuarios;
    //Constructor
    public DMABuscarUsuarioPorProyecto(ListView listado, Context context, int proyect_id)
    {
        this.listado = listado;
        this.context = context;
        this.id_proyecto=proyect_id;
    }

    @Override
    public String doInBackground(String... urls) {
        String result = "";
        listaUsuarios = new ArrayList<Voluntario>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String consulta = "SELECT u.ID AS UsuarioID, u.USERNAME AS UsuarioUsername, u.PASSWORD AS UsuarioPassword, u.PUNTUACION AS UsuarioPuntuacion, " +
                    "u.NOMBRE AS UsuarioNombre, u.APELLIDO AS UsuarioApellido, u.TELEFONO AS UsuarioTelefono, u.CORREO AS UsuarioCorreo, " +
                    "u.FECHA_NAC AS UsuarioFechaNac, u.CREACION AS UsuarioCreacion, u.ID_ESTADO AS UsuarioEstadoID, " +
                    "eu.ESTADO AS Estado, u.ID_TIPO AS UsuarioTipoID, tu.TIPO AS Tipo, " +
                    "up.FECHA_UNION AS FechaUnion, up.FECHA_SALIDA AS FechaSalida " +
                    "FROM USERS_PROYECTO up " +
                    "INNER JOIN USUARIOS u ON up.ID_USER = u.ID " +
                    "INNER JOIN ESTADOS_USUARIO eu ON u.ID_ESTADO = eu.ID " +
                    "INNER JOIN TIPOS_USUARIO tu ON u.ID_TIPO = tu.ID " +
                    "WHERE up.ID_PROYECTO = ? " +
                    "AND up.FECHA_SALIDA IS NULL";

            PreparedStatement preparedStatement = con.prepareStatement(consulta);
            preparedStatement.setInt(1, id_proyecto);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Voluntario voluntario = new Voluntario();
                voluntario.setId(resultSet.getInt("UsuarioID"));
                voluntario.setUsername(resultSet.getString("UsuarioUsername"));
                voluntario.setPassword(resultSet.getString("UsuarioPassword"));
                voluntario.setPuntuacion(resultSet.getInt("UsuarioPuntuacion"));
                voluntario.setNombre(resultSet.getString("UsuarioNombre"));
                voluntario.setApellido(resultSet.getString("UsuarioApellido"));
                voluntario.setTelefono(resultSet.getString("UsuarioTelefono"));
                voluntario.setCorreo(resultSet.getString("UsuarioCorreo"));
                voluntario.setFecha_nac(resultSet.getDate("UsuarioFechaNac"));
                voluntario.setFecha_alta(resultSet.getDate("UsuarioCreacion"));
                voluntario.setFecha_union(resultSet.getDate("FechaUnion"));
                voluntario.setFecha_salida(resultSet.getDate("FechaSalida"));
                voluntario.setActivo(voluntario.getFecha_salida()==null);
                voluntario.setEstado(new EstadoUsuario(resultSet.getInt("UsuarioEstadoID"), resultSet.getString("Estado")));
                voluntario.setTipo(new TipoUsuario(resultSet.getInt("UsuarioTipoID"), resultSet.getString("Tipo")));
                listaUsuarios.add(voluntario);

            }
            preparedStatement.close();
            con.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return "ERROR";
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        ListaVoluntariosAdapter adapter = new ListaVoluntariosAdapter(context,listaUsuarios);
        listado.setAdapter(adapter);
    }
}
