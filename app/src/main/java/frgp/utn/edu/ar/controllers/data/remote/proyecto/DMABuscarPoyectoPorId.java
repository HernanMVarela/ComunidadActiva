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

public class DMABuscarPoyectoPorId extends AsyncTask<Integer, Void, Proyecto> {
    private int id;
    private Proyecto proyecto = null;
    public DMABuscarPoyectoPorId(int id) {
        this.id = id;
    }
    @Override
    protected Proyecto doInBackground(Integer... integers) {
       try {

           Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String query = "SELECT P.ID, " +
                    "P.TITULO, " +
                    "P.DESCRIPCION, " +
                    "P.LATITUD, " +
                    "P.LONGITUD, " +
                    "P.CUPO, " +
                    "P.ID_USER, " +
                    "P.ID_TIPO AS PROYECTO_ID_TIPO, " +
                    "P.ID_ESTADO AS PROYECTO_ID_ESTADO, " +
                    "P.CONTACTO, " +
                    "P.AYUDA_ESPECIFICA, " +
                    "P.FECHA, " +
                    "U.USERNAME, " +
                    "U.PUNTUACION, " +
                    "U.NOMBRE, " +
                    "U.APELLIDO, " +
                    "U.TELEFONO, " +
                    "U.CORREO, " +
                    "U.FECHA_NAC, " +
                    "U.CREACION, " +
                    "U.ID_ESTADO AS USUARIO_ID_ESTADO, " +
                    "U.ID_TIPO AS USUARIO_ID_TIPO, " +
                    "TP.TIPO AS TIPO_PROYECTO, " +
                    "EP.ESTADO AS ESTADO_PROYECTO, " +
                    "UT.TIPO AS TIPO_USUARIO, " +
                    "EU.ESTADO AS ESTADO_USUARIO " +
                    "FROM PROYECTOS AS P " +
                    "INNER JOIN USUARIOS AS U ON P.ID_USER = U.ID " +
                    "INNER JOIN TIPOS_PROYECTO AS TP ON P.ID_TIPO = TP.ID " +
                    "INNER JOIN ESTADOS_PROYECTO AS EP ON P.ID_ESTADO = EP.ID " +
                    "INNER JOIN TIPOS_USUARIO AS UT ON U.ID_TIPO = UT.ID " +
                    "INNER JOIN ESTADOS_USUARIO AS EU ON U.ID_ESTADO = EU.ID " +
                    "WHERE P.ID = ?";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
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
                proyecto.setId(rs.getInt("ID"));
                proyecto.setTitulo(rs.getString("TITULO"));
                proyecto.setDescripcion(rs.getString("DESCRIPCION"));
                proyecto.setLatitud(rs.getDouble("LATITUD"));
                proyecto.setLongitud(rs.getDouble("LONGITUD"));
                proyecto.setCupo(rs.getInt("CUPO"));
                proyecto.setOwner(usuario);
                proyecto.setTipo(tipoProyecto);
                proyecto.setEstado(estadoProyecto);
                proyecto.setContacto(rs.getString("CONTACTO"));
                proyecto.setRequerimientos(rs.getString("AYUDA_ESPECIFICA"));
                proyecto.setFecha(rs.getDate("FECHA"));
            }
            rs.close();
            con.close();
            System.out.println("Proyecto encontrado: " + proyecto.getTitulo());
           return proyecto;
        } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
    }
}
