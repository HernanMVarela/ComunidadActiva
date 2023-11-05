package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.EstadoProyecto;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.model.TipoProyecto;
import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMABuscarPoyectoId extends AsyncTask<Integer, Void, Proyecto> {
    private Context context;
    private ListView listado;
    private static List<Proyecto> listaDeProyectos;
    private int proyectoID;
    private Proyecto proyectoEncontrado = new Proyecto();
    public DMABuscarPoyectoId(Context context) {
        this.context = context;
    }
    @Override
    protected Proyecto doInBackground(Integer... integers) {
        Proyecto proyectoEncontrado = null;
        if (integers.length > 0) {
            proyectoID = integers[0];
        } else {
            return null; // No se proporcionó un ID válido
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String query = "SELECT P.ID, P.TITULO, P.DESCRIPCION, P.LATITUD, P.LONGITUD, P.CUPO, P.ID_USER, P.ID_TIPO AS PROYECTO_ID_TIPO, P.ID_ESTADO AS PROYECTO_ID_ESTADO, P.CONTACTO, P.AYUDA_ESPECIFICA, " +
                    "U.USERNAME, U.PUNTUACION, U.NOMBRE, U.APELLIDO, U.TELEFONO, U.CORREO, U.FECHA_NAC, U.CREACION, U.ID_ESTADO AS USUARIO_ID_ESTADO, U.ID_TIPO AS USUARIO_ID_TIPO, " +
                    "TP.TIPO AS TIPO_PROYECTO, EP.ESTADO AS ESTADO_PROYECTO, " +
                    "UT.TIPO AS TIPO_USUARIO, EU.ESTADO AS ESTADO_USUARIO " +
                    "FROM PROYECTOS AS P " +
                    "INNER JOIN USUARIOS AS U ON P.ID_USER = U.ID " +
                    "INNER JOIN TIPOS_PROYECTO AS TP ON P.ID_TIPO = TP.ID " +
                    "INNER JOIN ESTADOS_PROYECTO AS EP ON P.ID_ESTADO = EP.ID " +
                    "INNER JOIN TIPOS_USUARIO AS UT ON U.ID_TIPO = UT.ID " +
                    "INNER JOIN ESTADOS_USUARIO AS EU ON U.ID_ESTADO = EU.ID" +
                    "WHERE P.ID = ?"; // Filtrar por el ID del proyecto

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, proyectoID);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                // Código para mapear el resultado a un objeto Proyecto (similar al que tienes en DMAListviewProyectos)

                TipoProyecto tipoProyecto = new TipoProyecto(rs.getInt("PROYECTO_ID_TIPO"), rs.getString("TIPO_PROYECTO"));
                EstadoProyecto estadoProyecto = new EstadoProyecto(rs.getInt("PROYECTO_ID_ESTADO"), rs.getString("ESTADO_PROYECTO"));

                // Crea objetos TipoUsuario y EstadoUsuario para el usuario
                TipoUsuario tipoUsuario = new TipoUsuario(rs.getInt("USUARIO_ID_TIPO"), rs.getString("TIPO_USUARIO"));
                EstadoUsuario estadoUsuario = new EstadoUsuario(rs.getInt("USUARIO_ID_ESTADO"), rs.getString("ESTADO_USUARIO"));

                // Crea el objeto Usuario y asigna los valores
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("ID_USER"));
                usuario.setUsername(rs.getString("USERNAME"));
                usuario.setPuntuacion(rs.getInt("PUNTUACION"));
                usuario.setNombre(rs.getString("NOMBRE"));
                usuario.setApellido(rs.getString("APELLIDO"));
                usuario.setTelefono(rs.getString("TELEFONO"));
                usuario.setCorreo(rs.getString("CORREO"));
                usuario.setFecha_nac(rs.getDate("FECHA_NAC"));
                usuario.setFecha_alta(rs.getDate("CREACION"));
                usuario.setEstado(estadoUsuario);
                usuario.setTipo(tipoUsuario);

                // Crea el objeto Proyecto y asigna los valores
                proyectoEncontrado.setId(rs.getInt("ID"));
                proyectoEncontrado.setTitulo(rs.getString("TITULO"));
                proyectoEncontrado.setDescripcion(rs.getString("DESCRIPCION"));
                proyectoEncontrado.setLatitud(rs.getDouble("LATITUD"));
                proyectoEncontrado.setLongitud(rs.getDouble("LONGITUD"));
                proyectoEncontrado.setCupo(rs.getInt("CUPO"));
                proyectoEncontrado.setOwner(usuario);
                proyectoEncontrado.setTipo(tipoProyecto);
                proyectoEncontrado.setEstado(estadoProyecto);
                proyectoEncontrado.setContacto(rs.getString("CONTACTO"));
                proyectoEncontrado.setRequerimientos(rs.getString("AYUDA_ESPECIFICA"));
            }
            rs.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proyectoEncontrado;
    }

    public Proyecto devolverProyecto(){
        return proyectoEncontrado;
    }

}
