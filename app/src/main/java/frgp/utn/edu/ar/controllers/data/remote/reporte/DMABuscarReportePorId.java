package frgp.utn.edu.ar.controllers.data.remote.reporte;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import frgp.utn.edu.ar.controllers.data.model.EstadoReporte;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.TipoReporte;
import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMABuscarReportePorId extends AsyncTask<String, Void, Reporte> {

    private Reporte cargado = null;
    private int id_reporte;
    //Constructor
    public DMABuscarReportePorId(int id_reporte)
    {
        this.id_reporte = id_reporte;
    }

    @Override
    protected Reporte doInBackground(String... urls) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String query = "SELECT R.ID AS ReporteID, R.TITULO AS TituloReporte, R.DESCRIPCION AS DescripcionReporte, " +
                    "R.LATITUD AS LatitudReporte, R.LONGITUD AS LongitudReporte, R.FECHA AS FechaReporte, " +
                    "R.CANT_VOTOS AS CantidadVotos, R.Puntaje AS PuntajeReporte, R.ID_USER AS IDUsuarioReporte, " +
                    "R.ID_TIPO AS IDTipoReporte, R.ID_ESTADO AS IDEstadoReporte, U.USERNAME AS UsernameUsuario, " +
                    "U.NOMBRE AS NombreUsuario, U.PUNTUACION, U.APELLIDO AS ApellidoUsuario, U.TELEFONO AS TelefonoUsuario, " +
                    "U.CORREO AS CorreoUsuario, U.FECHA_NAC AS FechaNacimientoUsuario, U.CREACION AS FechaCreacionUsuario, " +
                    "TR.TIPO AS TipoReporte, ER.ESTADO AS EstadoReporte FROM REPORTES AS R " +
                    "INNER JOIN USUARIOS AS U ON R.ID_USER = U.ID INNER JOIN TIPOS_REPORTE AS TR ON R.ID_TIPO = TR.ID " +
                    "INNER JOIN ESTADOS_REPORTE AS ER ON R.ID_ESTADO = ER.ID WHERE R.ID = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,id_reporte);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Reporte reporte = new Reporte();
                reporte.setId(rs.getInt("ReporteID"));
                reporte.setTitulo(rs.getString("TituloReporte"));
                reporte.setDescripcion(rs.getString("DescripcionReporte"));
                reporte.setLatitud(rs.getDouble("LatitudReporte"));
                reporte.setLongitud(rs.getDouble("LongitudReporte"));
                reporte.setFecha(rs.getDate("FechaReporte"));
                reporte.setImagen(null);
                reporte.setCant_votos(rs.getInt("CantidadVotos"));
                reporte.setPuntaje(rs.getInt("PuntajeReporte"));
                Usuario user = new Usuario();
                user.setId(rs.getInt("IDUsuarioReporte"));
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
                TipoReporte tipo = new TipoReporte(rs.getInt("IDTipoReporte"), rs.getString("TipoReporte"));
                EstadoReporte estado = new EstadoReporte(rs.getInt("IDEstadoReporte"), rs.getString("EstadoReporte"));
                reporte.setTipo(tipo);
                reporte.setEstado(estado);
                reporte.setOwner(user);
                cargado = reporte;
            }
            ps.close();
            con.close();
            return cargado;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
