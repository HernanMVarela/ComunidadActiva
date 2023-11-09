package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import frgp.utn.edu.ar.controllers.data.model.EstadoProyecto;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.model.TipoProyecto;
import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMABuscarProyectoPorId extends AsyncTask<Integer, Void, Proyecto> {
    private int id;
    private Proyecto proyecto = null;
    public DMABuscarProyectoPorId(int id) {
        this.id = id;
    }
    @Override
    protected Proyecto doInBackground(Integer... integers) {
       try {
           System.out.println("BUSCANDO PROYECTO");

           Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String query = "SELECT " +
                    "P.ID AS ProyectoID, " +
                    "P.TITULO AS TituloProyecto, " +
                    "P.DESCRIPCION AS DescripcionProyecto, " +
                    "P.LATITUD AS LatitudProyecto, " +
                    "P.LONGITUD AS LongitudProyecto, " +
                    "P.CUPO AS CupoProyecto, " +
                    "P.ID_USER AS IDUsuarioProyecto, " +
                    "P.ID_TIPO AS IDTipoProyecto, " +
                    "P.ID_ESTADO AS IDEstadoProyecto, " +
                    "P.CONTACTO AS ContactoProyecto, " +
                    "P.AYUDA_ESPECIFICA AS AyudaEspecifica, " +
                    "P.FECHA AS FechaProyecto, " +
                    "U.USERNAME AS UsernameUsuario, " +
                    "U.NOMBRE AS NombreUsuario, " +
                    "U.PUNTUACION, " +
                    "U.APELLIDO AS ApellidoUsuario, " +
                    "U.TELEFONO AS TelefonoUsuario, " +
                    "U.CORREO AS CorreoUsuario, " +
                    "U.FECHA_NAC AS FechaNacimientoUsuario, " +
                    "U.CREACION AS FechaCreacionUsuario, " +
                    "TP.TIPO AS TipoProyecto, " +
                    "EP.ESTADO AS EstadoProyecto " +
                    "FROM PROYECTOS AS P " +
                    "INNER JOIN USUARIOS AS U ON P.ID_USER = U.ID " +
                    "INNER JOIN TIPOS_PROYECTO AS TP ON P.ID_TIPO = TP.ID " +
                    "INNER JOIN ESTADOS_PROYECTO AS EP ON P.ID_ESTADO = EP.ID " +
                    "WHERE P.ID = ?";


            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            Proyecto proyecto = null;

            if (rs.next()) {
                proyecto = new Proyecto();
                proyecto.setId(rs.getInt("ProyectoID"));
                proyecto.setTitulo(rs.getString("TituloProyecto"));
                proyecto.setDescripcion(rs.getString("DescripcionProyecto"));
                proyecto.setLatitud(rs.getDouble("LatitudProyecto"));
                proyecto.setLongitud(rs.getDouble("LongitudProyecto"));
                proyecto.setCupo(rs.getInt("CupoProyecto"));
                Usuario user = new Usuario();
                user.setId(rs.getInt("IDUsuarioProyecto"));
                user.setUsername(rs.getString("UsernameUsuario"));
                user.setNombre(rs.getString("NombreUsuario"));
                user.setApellido(rs.getString("ApellidoUsuario"));
                user.setTelefono(rs.getString("TelefonoUsuario"));
                user.setCorreo(rs.getString("CorreoUsuario"));
                user.setFecha_nac(rs.getDate("FechaNacimientoUsuario"));
                user.setFecha_alta(rs.getDate("FechaCreacionUsuario"));
                user.setPuntuacion(rs.getInt("PUNTUACION"));
                user.setTipo(new TipoUsuario());
                user.setEstado(new EstadoUsuario());
                proyecto.setOwner(user);
                TipoProyecto tipoProyecto = new TipoProyecto();
                tipoProyecto.setId(rs.getInt("IDTipoProyecto"));
                tipoProyecto.setTipo(rs.getString("TipoProyecto"));
                proyecto.setTipo(tipoProyecto);
                EstadoProyecto estadoProyecto = new EstadoProyecto();
                estadoProyecto.setId(rs.getInt("IDEstadoProyecto"));
                estadoProyecto.setEstado(rs.getString("EstadoProyecto"));
                proyecto.setEstado(estadoProyecto);
                proyecto.setContacto(rs.getString("ContactoProyecto"));
                proyecto.setRequerimientos(rs.getString("AyudaEspecifica"));
                proyecto.setFecha(rs.getDate("FechaProyecto"));
            }
            rs.close();
            con.close();
           return proyecto;
        } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
    }
}
