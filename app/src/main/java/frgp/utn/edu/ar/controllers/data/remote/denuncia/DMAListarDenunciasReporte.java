package frgp.utn.edu.ar.controllers.data.remote.denuncia;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoDenuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Publicacion;
import frgp.utn.edu.ar.controllers.data.model.TipoDenuncia;
import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.ui.adapters.ListarDenunciaAdapter;


public class DMAListarDenunciasReporte extends AsyncTask<String, Void, List<Denuncia>> {


    public DMAListarDenunciasReporte(){ }

    @Override
    protected List<Denuncia> doInBackground(String... strings) {
        List <Denuncia> listaDenuncias = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String query = "SELECT " +
                    "D.ID_REPORTE AS IDReporte, " +
                    "D.ID_USER AS IDUser, " +
                    "D.ID_ESTADO AS IDEstado, " +
                    "D.TITULO AS Titulo, " +
                    "D.DESCRIPCION AS Descripcion, " +
                    "D.FECHA_CREACION AS FechaCreacion, " +
                    "ED.ESTADO AS EstadoDenuncia, " +
                    "R.TITULO AS TituloPublicacion, " +
                    "R.DESCRIPCION AS DescripcionPublicacion, " +
                    "R.LONGITUD AS LongitudPublicacion, " +
                    "R.LATITUD AS LatitudPublicacion, " +
                    "R.FECHA AS FechaPublicacion, " +
                    "U.USERNAME AS UsernameUsuario, " +
                    "U.NOMBRE AS NombreUsuario, " +
                    "U.APELLIDO AS ApellidoUsuario, " +
                    "U.TELEFONO AS TelefonoUsuario, " +
                    "U.CORREO AS CorreoUsuario, " +
                    "U.FECHA_NAC AS FechaNacimientoUsuario, " +
                    "U.CREACION AS FechaCreacionUsuario, " +
                    "UP.ID AS IDUserPublicacion, " +
                    "UP.USERNAME AS UsernameUsuarioPublicacion, " +
                    "UP.NOMBRE AS NombreUsuarioPublicacion, " +
                    "UP.APELLIDO AS ApellidoUsuarioPublicacion, " +
                    "UP.TELEFONO AS TelefonoUsuarioPublicacion, " +
                    "UP.CORREO AS CorreoUsuarioPublicacion, " +
                    "UP.FECHA_NAC AS FechaNacimientoUsuarioPublicacion, " +
                    "UP.CREACION AS FechaCreacionUsuarioPublicacion, " +
                    "UP.ID_TIPO AS IDTipoUsuarioPublicacion, " +
                    "UP.ID_ESTADO AS IDEstadoUserPublicacion, " +
                    "EUP.ESTADO AS EstadoUsuarioPublicacion, " +
                    "TUP.TIPO AS TipoUsuarioPublicacion " +
                    "FROM DENUNCIAS_REPORTES AS D " +
                    "INNER JOIN REPORTES AS R ON D.ID_REPORTE = R.ID " +
                    "INNER JOIN USUARIOS AS U ON D.ID_USER = U.ID " +
                    "INNER JOIN ESTADOS_DENUNCIA AS ED ON D.ID_ESTADO = ED.ID " +
                    "INNER JOIN USUARIOS AS UP ON R.ID_USER = UP.ID " +
                    "INNER JOIN ESTADOS_USUARIO AS EUP ON UP.ID_ESTADO = EUP.ID " +
                    "INNER JOIN TIPOS_USUARIO AS TUP ON UP.ID_TIPO = TUP.ID " +
                    "ORDER BY FECHA_CREACION;";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            listaDenuncias = new ArrayList<>();

            while (rs.next()) {
                Denuncia denuncia = new Denuncia();
                Usuario userPublicacion = new Usuario();
                Publicacion publicacion = new Publicacion();
                Usuario user = new Usuario();
                EstadoDenuncia estadoDenuncia = new EstadoDenuncia(rs.getInt("IDEstado"), rs.getString("EstadoDenuncia"));
                EstadoUsuario estadoUsuarioPublicacion = new EstadoUsuario(rs.getInt("IDEstadoUserPublicacion"), rs.getString("EstadoUsuarioPublicacion"));
                TipoUsuario tipoUsuarioPublicacion = new TipoUsuario(rs.getInt("IDTipoUsuarioPublicacion"),rs.getString("TipoUsuarioPublicacion"));
                TipoDenuncia tipoDenuncia = new TipoDenuncia(1,"REPORTE");

                userPublicacion.setId(rs.getInt("IDUserPublicacion"));
                userPublicacion.setUsername(rs.getString("UsernameUsuarioPublicacion"));
                userPublicacion.setNombre(rs.getString("NombreUsuarioPublicacion"));
                userPublicacion.setApellido(rs.getString("ApellidoUsuarioPublicacion"));
                userPublicacion.setTelefono(rs.getString("TelefonoUsuarioPublicacion"));
                userPublicacion.setCorreo(rs.getString("CorreoUsuarioPublicacion"));
                userPublicacion.setFecha_nac(rs.getDate("FechaNacimientoUsuarioPublicacion"));
                userPublicacion.setFecha_alta(rs.getDate("FechaCreacionUsuarioPublicacion"));
                userPublicacion.setEstado(estadoUsuarioPublicacion);
                userPublicacion.setTipo(tipoUsuarioPublicacion);

                publicacion.setId(rs.getInt("IDReporte"));
                publicacion.setDescripcion(rs.getString("DescripcionPublicacion"));
                publicacion.setTitulo(rs.getString("TituloPublicacion"));
                publicacion.setFecha(rs.getDate("FechaPublicacion"));
                publicacion.setLatitud(rs.getDouble("LatitudPublicacion"));
                publicacion.setLongitud(rs.getDouble("LongitudPublicacion"));
                publicacion.setOwner(userPublicacion);

                user.setId(rs.getInt("IDUser"));
                user.setUsername(rs.getString("UsernameUsuario"));
                user.setNombre(rs.getString("NombreUsuario"));
                user.setApellido(rs.getString("ApellidoUsuario"));
                user.setTelefono(rs.getString("TelefonoUsuario"));
                user.setCorreo(rs.getString("CorreoUsuario"));
                user.setFecha_nac(rs.getDate("FechaNacimientoUsuario"));
                user.setFecha_alta(rs.getDate("FechaCreacionUsuario"));

                denuncia.setDescripcion(rs.getString("Descripcion"));
                denuncia.setTitulo(rs.getString("Titulo"));
                denuncia.setFecha_creacion(rs.getDate("FechaCreacion"));
                denuncia.setEstado(estadoDenuncia);
                denuncia.setDenunciante(user);
                denuncia.setPublicacion(publicacion);
                denuncia.setTipo(tipoDenuncia);

                listaDenuncias.add(denuncia);
            }
            preparedStatement.close();
            con.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return listaDenuncias;
    }
}
