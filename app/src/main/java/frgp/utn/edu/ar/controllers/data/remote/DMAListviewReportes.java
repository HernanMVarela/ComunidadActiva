package frgp.utn.edu.ar.controllers.data.remote;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
    private GoogleMap mapa;
    private static String result2;
    private static List<Reporte> listaReporte;

    //Constructor
    public DMAListviewReportes(ListView listview, Context ct, LatLng ubicacion, GoogleMap mapa) {
        listado = listview;
        context = ct;
        this.mapa = mapa;
        this.ubicacion = ubicacion;
        Log.i("Coords","Lat: " + this.ubicacion.latitude + " | Long: " + this.ubicacion.longitude);
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            double dispositivoLatitud = ubicacion.latitude;
            double dispositivoLongitud = ubicacion.longitude;

            String query = "SELECT " +
                    "R.ID AS ReporteID, " +
                    "R.TITULO AS TituloReporte, " +
                    "R.DESCRIPCION AS DescripcionReporte, " +
                    "R.LATITUD AS LatitudReporte, " +
                    "R.LONGITUD AS LongitudReporte, " +
                    "R.FECHA AS FechaReporte, " +
                    "R.CANT_VOTOS AS CantidadVotos, " +
                    "R.PUNTAJE AS PuntajeReporte, " +
                    "R.ID_USER AS IDUsuarioReporte, " +
                    "R.ID_TIPO AS IDTipoReporte, " +
                    "R.ID_ESTADO AS IDEstadoReporte, " +
                    "U.USERNAME AS UsernameUsuario, " +
                    "U.NOMBRE AS NombreUsuario, " +
                    "U.APELLIDO AS ApellidoUsuario, " +
                    "U.TELEFONO AS TelefonoUsuario, " +
                    "U.CORREO AS CorreoUsuario, " +
                    "U.FECHA_NAC AS FechaNacimientoUsuario, " +
                    "U.CREACION AS FechaCreacionUsuario, " +
                    "TR.TIPO AS TipoReporte, " +
                    "ER.ESTADO AS EstadoReporte, " +
                    "6371 * 2 * ASIN(SQRT(" +
                    "POW(SIN(RADIANS(R.LATITUD - ?) / 2), 2) + " +
                    "COS(RADIANS(?)) * COS(RADIANS(R.LATITUD)) * " +
                    "POW(SIN(RADIANS(R.LONGITUD - ?) / 2), 2)" +
                    ")) AS Distancia " +
                    "FROM REPORTES AS R " +
                    "INNER JOIN USUARIOS AS U ON R.ID_USER = U.ID " +
                    "INNER JOIN TIPOS_REPORTE AS TR ON R.ID_TIPO = TR.ID " +
                    "INNER JOIN ESTADOS_REPORTE AS ER ON R.ID_ESTADO = ER.ID " +
                    "HAVING Distancia <= 5 " +
                    "ORDER BY Distancia;";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setDouble(1, dispositivoLatitud); // Nueva ubicación del dispositivo
            preparedStatement.setDouble(2, dispositivoLatitud); // Nueva ubicación del dispositivo (repetir para el cálculo)
            preparedStatement.setDouble(3, dispositivoLongitud); // Nueva ubicación del dispositivo

            ResultSet rs = preparedStatement.executeQuery();
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
        assert listaReporte != null;
        marcarUbicaciones();
        listado.setAdapter(adapter);
    }

    private void marcarUbicaciones(){
        for (Reporte item: listaReporte) {
            LatLng repUbi = new LatLng(item.getLatitud(),item.getLongitud());
            mapa.addMarker(new MarkerOptions().position(repUbi).title(item.getTitulo()));
        }
    }

}
