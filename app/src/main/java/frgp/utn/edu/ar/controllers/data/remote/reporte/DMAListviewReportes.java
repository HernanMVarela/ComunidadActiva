package frgp.utn.edu.ar.controllers.data.remote.reporte;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.EstadoReporte;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.TipoReporte;
import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.ui.adapters.ListaReportesAdapter;
import frgp.utn.edu.ar.controllers.ui.adapters.TipoReporteAdapter;

public class DMAListviewReportes extends AsyncTask<String, Void, String> {

    private Context context;
    private ListView listado;
    private LatLng ubicacion;
    private Usuario loggedUser;
    private boolean todo;
    private GoogleMap mapa;
    private static String result2;
    private static List<Reporte> listaReporte;

    //Constructor
    public DMAListviewReportes(ListView listview, Context ct, LatLng ubicacion, GoogleMap mapa, Usuario loggedUser, boolean todo) {
        listado = listview;
        context = ct;
        this.mapa = mapa;
        this.loggedUser = loggedUser;
        this.todo = todo;
        this.ubicacion = ubicacion;
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();

            PreparedStatement preparedStatement = generadorConsulta(con,loggedUser.getTipo().getTipo(),todo);
            assert preparedStatement != null;
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

    private void marcarUbicaciones() {
        for (Reporte item : listaReporte) {
            LatLng repUbi = new LatLng(item.getLatitud(), item.getLongitud());
            int iconResource;

            if (item.getTipo().getTipo().equals("BASURA")) {
                iconResource = R.drawable.garbage_bag;
            } else if (item.getTipo().getTipo().equals("ILUMINACION")) {
                iconResource = R.drawable.broken_bulb;
            } else if (item.getTipo().getTipo().equals("BACHE")) {
                iconResource = R.drawable.pothole;
            } else if (item.getTipo().getTipo().equals("ARBOL CAIDO")) {
                iconResource = R.drawable.tree;
            }else if (item.getTipo().getTipo().equals("ANIMALES SUELTOS")) {
                iconResource = R.drawable.animales;
            } else if (item.getTipo().getTipo().equals("FUGA DE AGUA")) {
                iconResource = R.drawable.fuga_agua;
            } else if (item.getTipo().getTipo().equals("CABLES CAIDOS")) {
                iconResource = R.drawable.cables_sueltos;
            } else if (item.getTipo().getTipo().equals("PARQUE DESCUIDADO")) {
                iconResource = R.drawable.parque;
            } else if (item.getTipo().getTipo().equals("CONTAMINACION")) {
                iconResource = R.drawable.contaminacion;
            }  else {
                iconResource = 0;
            }

            if (iconResource != 0) {
                // Carga el ícono SVG y cambia su color en tiempo real
                Drawable drawable = VectorDrawableCompat.create(context.getResources(), iconResource, null);
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable, getColorForReportType(item.getTipo().getTipo()));
                // Configura el tamaño del ícono
                int width = 50; // Ancho en píxeles
                int height = 50; // Alto en píxeles
                // Converte el Drawable a Bitmap
                BitmapDescriptor icon = getBitmapDescriptorFromDrawable(drawable, width, height);
                // Agrega el marcador al mapa
                mapa.addMarker(new MarkerOptions()
                        .position(repUbi)
                        .title(item.getTipo().getTipo())
                        .icon(icon)
                );
            }
        }
    }

    private int getColorForReportType(String reportType) {
        // Define los colores para cada tipo de reporte
        if (reportType.equals("BASURA")) {
            return Color.BLUE;
        } else if (reportType.equals("ILUMINACION")) {
            return Color.RED;
        } else if (reportType.equals("BACHE")) {
            return Color.rgb(0, 128, 0);
        } else if (reportType.equals("VEREDA EN MAL ESTADO")) {
            return Color.rgb(218, 165, 32);
        } else if (reportType.equals("ARBOL CAIDO")) {
            return Color.rgb(0, 100, 0);
        } else if (reportType.equals("ANIMALES SUELTOS")) {
            return Color.rgb(255, 165, 0);
        } else if (reportType.equals("FUGA DE AGUA")) {
            return Color.CYAN;
        } else if (reportType.equals("CABLES CAIDOS")) {
            return Color.DKGRAY;
        } else if (reportType.equals("PARQUE DESCUIDADO")) {
            return Color.GREEN;
        } else if (reportType.equals("CONTAMINACION")) {
            return Color.MAGENTA;
        } else {
            return Color.BLACK; // Color por defecto (negro)
        }
    }

    private BitmapDescriptor getBitmapDescriptorFromDrawable(Drawable drawable, int width, int height) {
        // Convierte el Drawable a Bitmap y lo redimensiona
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private PreparedStatement generadorConsulta(Connection con, String userType, boolean todo){
        double dispositivoLatitud = ubicacion.latitude;
        double dispositivoLongitud = ubicacion.longitude;
        String query = "";
        if(userType.equals("VECINO") && todo){
            query = "SELECT R.ID AS ReporteID, R.TITULO AS TituloReporte, R.DESCRIPCION AS DescripcionReporte, " +
                    "R.LATITUD AS LatitudReporte, R.LONGITUD AS LongitudReporte, R.FECHA AS FechaReporte, " +
                    "R.CANT_VOTOS AS CantidadVotos, R.Puntaje AS PuntajeReporte, R.ID_USER AS IDUsuarioReporte, " +
                    "R.ID_TIPO AS IDTipoReporte, R.ID_ESTADO AS IDEstadoReporte, U.USERNAME AS UsernameUsuario, " +
                    "U.NOMBRE AS NombreUsuario, U.APELLIDO AS ApellidoUsuario, U.TELEFONO AS TelefonoUsuario, " +
                    "U.CORREO AS CorreoUsuario, U.FECHA_NAC AS FechaNacimientoUsuario, U.CREACION AS FechaCreacionUsuario, " +
                    "TR.TIPO AS TipoReporte, ER.ESTADO AS EstadoReporte, " +
                    "6371 * 2 * ASIN(SQRT(POW(SIN(RADIANS(R.LATITUD - ?) / 2), 2) + " +
                    "COS(RADIANS(?)) * COS(RADIANS(R.LATITUD)) * POW(SIN(RADIANS(R.LONGITUD - ?) / 2), 2)" +
                    ")) AS Distancia FROM REPORTES AS R " +
                    "INNER JOIN USUARIOS AS U ON R.ID_USER = U.ID " +
                    "INNER JOIN TIPOS_REPORTE AS TR ON R.ID_TIPO = TR.ID " +
                    "INNER JOIN ESTADOS_REPORTE AS ER ON R.ID_ESTADO = ER.ID " +
                    "WHERE ER.ESTADO NOT IN ('CERRADO', 'DENUNCIADO') " +
                    "AND R.FECHA >= DATE_SUB(NOW(), INTERVAL 3 MONTH) " +
                    "HAVING Distancia <= 10 " +
                    "ORDER BY Distancia LIMIT 15;";
        }
        if(userType.equals("VECINO") && !todo ){
            query = "SELECT R.ID AS ReporteID, R.TITULO AS TituloReporte, R.DESCRIPCION AS DescripcionReporte, " +
                    "R.LATITUD AS LatitudReporte, R.LONGITUD AS LongitudReporte, R.FECHA AS FechaReporte, " +
                    "R.CANT_VOTOS AS CantidadVotos, R.PUNTAJE AS PuntajeReporte, R.ID_USER AS IDUsuarioReporte, " +
                    "R.ID_TIPO AS IDTipoReporte, R.ID_ESTADO AS IDEstadoReporte, U.USERNAME AS UsernameUsuario, " +
                    "U.NOMBRE AS NombreUsuario, U.APELLIDO AS ApellidoUsuario, U.TELEFONO AS TelefonoUsuario, " +
                    "U.CORREO AS CorreoUsuario, U.FECHA_NAC AS FechaNacimientoUsuario, U.CREACION AS FechaCreacionUsuario, " +
                    "TR.TIPO AS TipoReporte, ER.ESTADO AS EstadoReporte, " +
                    "6371 * 2 * ASIN(SQRT(POW(SIN(RADIANS(R.LATITUD - ?) / 2), 2) + " +
                    "COS(RADIANS(?)) * COS(RADIANS(R.LATITUD)) * POW(SIN(RADIANS(R.LONGITUD - ?) / 2), 2)" +
                    ")) AS Distancia FROM REPORTES AS R INNER JOIN USUARIOS AS U ON R.ID_USER = U.ID " +
                    "INNER JOIN TIPOS_REPORTE AS TR ON R.ID_TIPO = TR.ID INNER JOIN ESTADOS_REPORTE AS ER ON R.ID_ESTADO = ER.ID " +
                    "WHERE ER.ESTADO = 'ABIERTO' AND R.FECHA >= DATE_SUB(NOW(), INTERVAL 3 MONTH) " +
                    "HAVING Distancia <= 10 ORDER BY Distancia LIMIT 15;";
        }
        if ((userType.equals("MODERADOR") || userType.equals("ADMINISTRADOR"))&& todo) {
            query = "SELECT R.ID AS ReporteID, R.TITULO AS TituloReporte, R.DESCRIPCION AS DescripcionReporte, " +
                    "R.LATITUD AS LatitudReporte, R.LONGITUD AS LongitudReporte, R.FECHA AS FechaReporte, " +
                    "R.CANT_VOTOS AS CantidadVotos, R.PUNTAJE AS PuntajeReporte, R.ID_USER AS IDUsuarioReporte, " +
                    "R.ID_TIPO AS IDTipoReporte, R.ID_ESTADO AS IDEstadoReporte, U.USERNAME AS UsernameUsuario, " +
                    "U.NOMBRE AS NombreUsuario, U.APELLIDO AS ApellidoUsuario, U.TELEFONO AS TelefonoUsuario, " +
                    "U.CORREO AS CorreoUsuario, U.FECHA_NAC AS FechaNacimientoUsuario, U.CREACION AS FechaCreacionUsuario, " +
                    "TR.TIPO AS TipoReporte, ER.ESTADO AS EstadoReporte, " +
                    "6371 * 2 * ASIN(SQRT(POW(SIN(RADIANS(R.LATITUD - ?) / 2), 2) + " +
                    "COS(RADIANS(?)) * COS(RADIANS(R.LATITUD)) * POW(SIN(RADIANS(R.LONGITUD - ?) / 2), 2)" +
                    ")) AS Distancia FROM REPORTES AS R INNER JOIN USUARIOS AS U ON R.ID_USER = U.ID " +
                    "INNER JOIN TIPOS_REPORTE AS TR ON R.ID_TIPO = TR.ID INNER JOIN ESTADOS_REPORTE AS ER ON R.ID_ESTADO = ER.ID " +
                    "WHERE ER.ESTADO = 'ABIERTO' AND R.FECHA >= DATE_SUB(NOW(), INTERVAL 3 MONTH) " +
                    "HAVING Distancia <= 10 ORDER BY Distancia LIMIT 15;";
        }
        if ((userType.equals("MODERADOR") || userType.equals("ADMINISTRADOR"))&& !todo) {
            query = "SELECT R.ID AS ReporteID, R.TITULO AS TituloReporte, R.DESCRIPCION AS DescripcionReporte, " +
                    "R.LATITUD AS LatitudReporte, R.LONGITUD AS LongitudReporte, R.FECHA AS FechaReporte, " +
                    "R.CANT_VOTOS AS CantidadVotos, R.PUNTAJE AS PuntajeReporte, R.ID_USER AS IDUsuarioReporte, " +
                    "R.ID_TIPO AS IDTipoReporte, R.ID_ESTADO AS IDEstadoReporte, U.USERNAME AS UsernameUsuario, " +
                    "U.NOMBRE AS NombreUsuario, U.APELLIDO AS ApellidoUsuario, U.TELEFONO AS TelefonoUsuario, " +
                    "U.CORREO AS CorreoUsuario, U.FECHA_NAC AS FechaNacimientoUsuario, U.CREACION AS FechaCreacionUsuario, " +
                    "TR.TIPO AS TipoReporte, ER.ESTADO AS EstadoReporte, " +
                    "6371 * 2 * ASIN(SQRT(POW(SIN(RADIANS(R.LATITUD - ?) / 2), 2) + " +
                    "COS(RADIANS(?)) * COS(RADIANS(R.LATITUD)) * POW(SIN(RADIANS(R.LONGITUD - ?) / 2), 2)" +
                    ")) AS Distancia FROM REPORTES AS R INNER JOIN USUARIOS AS U ON R.ID_USER = U.ID " +
                    "INNER JOIN TIPOS_REPORTE AS TR ON R.ID_TIPO = TR.ID INNER JOIN ESTADOS_REPORTE AS ER ON R.ID_ESTADO = ER.ID " +
                    "WHERE R.FECHA >= DATE_SUB(NOW(), INTERVAL 3 MONTH) " +
                    "HAVING Distancia <= 10 ORDER BY Distancia LIMIT 15;";
        }
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setDouble(1, dispositivoLatitud); // Nueva ubicación del dispositivo
            preparedStatement.setDouble(2, dispositivoLatitud); // Nueva ubicación del dispositivo (repetir para el cálculo)
            preparedStatement.setDouble(3, dispositivoLongitud); // Nueva ubicación del dispositivo
            return preparedStatement;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
