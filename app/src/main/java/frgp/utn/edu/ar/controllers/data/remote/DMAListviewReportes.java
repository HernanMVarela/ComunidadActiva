package frgp.utn.edu.ar.controllers.data.remote;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.EstadoReporte;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.TipoReporte;
import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.ui.adapters.ListaReportesAdapter;
import frgp.utn.edu.ar.controllers.ui.adapters.TipoReporteAdapter;

public class DMAListviewReportes extends AsyncTask<String, Void, String> {

    private Context context;
    private ListView listado;
    private LatLng ubicacion;
    private static String result2;
    private static List<Reporte> listaReporte;
    private FusedLocationProviderClient fusedLocationClient;

    //Constructor
    public DMAListviewReportes(ListView listview, Context ct, LatLng ubicacion) {
        listado = listview;
        context = ct;
        this.ubicacion = ubicacion;
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT\n" +
                    "    R.ID AS ReporteID,\n" +
                    "    R.TITULO AS TituloReporte,\n" +
                    "    R.DESCRIPCION AS DescripcionReporte,\n" +
                    "    R.LATITUD AS LatitudReporte,\n" +
                    "    R.LONGITUD AS LongitudReporte,\n" +
                    "    R.IMAGEN AS ImagenReporte,\n" + //// SIN OBTENER TODAVIA
                    "    R.FECHA AS FechaReporte,\n" +
                    "    R.CANT_VOTOS AS CantidadVotos,\n" +
                    "    R.PUNTAJE AS PuntajeReporte,\n" +
                    "    R.ID_USER AS IDUsuarioReporte,\n" +
                    "    R.ID_TIPO AS IDTipoReporte,\n" +
                    "    R.ID_ESTADO AS IDEstadoReporte,\n" +
                    "    U.USERNAME AS UsernameUsuario,\n" +
                    "    U.NOMBRE AS NombreUsuario,\n" +
                    "    U.APELLIDO AS ApellidoUsuario,\n" +
                    "    U.TELEFONO AS TelefonoUsuario,\n" +
                    "    U.CORREO AS CorreoUsuario,\n" +
                    "    U.FECHA_NAC AS FechaNacimientoUsuario,\n" +
                    "    U.CREACION AS FechaCreacionUsuario,\n" +
                    "    TR.TIPO AS TipoReporte,\n" +
                    "    ER.ESTADO AS EstadoReporte\n" +
                    "FROM\n" +
                    "    REPORTES AS R\n" +
                    "INNER JOIN\n" +
                    "    USUARIOS AS U ON R.ID_USER = U.ID\n" +
                    "INNER JOIN\n" +
                    "    TIPOS_REPORTE AS TR ON R.ID_TIPO = TR.ID\n" +
                    "INNER JOIN\n" +
                    "    ESTADOS_REPORTE AS ER ON R.ID_ESTADO = ER.ID;");
            result2 = " ";
            listaReporte = new ArrayList<Reporte>();
            while (rs.next()) {
                Reporte reporte = new Reporte();
                reporte.setId(rs.getInt("ReporteID"));
                reporte.setTitulo(rs.getString("TituloReporte"));
                reporte.setDescripcion(rs.getString("DescripcionReporte"));
                reporte.setLatitud(rs.getDouble("LatitudReporte"));
                reporte.setLongitud(rs.getDouble("LongitudReporte"));
                reporte.setFecha(rs.getDate("FechaReporte"));
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
                user.setTipo(new TipoUsuario());
                user.setEstado(new EstadoUsuario());
                TipoReporte tipo = new TipoReporte(rs.getInt("IDTipoReporte"), rs.getString("TipoReporte"));
                EstadoReporte estado = new EstadoReporte(rs.getInt("IDEstadoReporte"), rs.getString("EstadoReporte"));

                reporte.setOwner(user);
                reporte.setTipo(tipo);
                reporte.setEstado(estado);

                listaReporte.add(reporte);
            }
            response = "Conexion exitosa";
        } catch (Exception e) {
            e.printStackTrace();
            result2 = "Conexion no exitosa";
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        ListaReportesAdapter adapter = new ListaReportesAdapter(context, listaReporte, ubicacion);
        listado.setAdapter(adapter);
    }

}
