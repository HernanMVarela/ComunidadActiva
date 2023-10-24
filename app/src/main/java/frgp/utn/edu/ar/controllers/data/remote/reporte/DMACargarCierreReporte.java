package frgp.utn.edu.ar.controllers.data.remote.reporte;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import frgp.utn.edu.ar.controllers.data.model.CierreReporte;
import frgp.utn.edu.ar.controllers.data.model.EstadoReporte;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMACargarCierreReporte extends AsyncTask<String, Void, String> {

    private Context context;
    private CierreReporte leerCierre;
    private TextView atendido, motivo;
    private ImageView imagen;
    private int id_reporte;
    private static String result2;
    private int dataRowModif;

    //Constructor
    public DMACargarCierreReporte(TextView atendido, TextView motivo, ImageView imagen, int id_reporte, Context ct)
    {
        this.atendido = atendido;
        this.motivo = motivo;
        this.imagen = imagen;
        this.id_reporte = id_reporte;
        this.context = ct;
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String query = "SELECT r.ID, r.TITULO, r.DESCRIPCION, " +
                    "c.MOTIVO_CIERRE, c.FECHA_CIERRE, c.IMAGEN AS CIERRE_IMAGEN, c.ID_ESTADOCIERRE, c.ID_USER, " +
                    "u.USERNAME, u.NOMBRE, u.APELLIDO, e.ESTADO " +
                    "FROM CIERRES_REPORTE c " +
                    "INNER JOIN REPORTES r ON c.ID_REPORTE = r.ID " +
                    "INNER JOIN USUARIOS u ON c.ID_USER = u.ID " +
                    "INNER JOIN ESTADOS_REPORTE e ON c.ID_ESTADOCIERRE = e.ID " +
                    "WHERE r.ID = ? " +
                    "ORDER BY c.FECHA_CIERRE " +
                    "LIMIT 1;";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,id_reporte);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                // Crear una instancia de Usuario
                Usuario usuario = new Usuario();
                usuario.setId(resultSet.getInt("ID_USER"));
                usuario.setUsername(resultSet.getString("USERNAME"));
                usuario.setNombre(resultSet.getString("NOMBRE"));
                usuario.setApellido(resultSet.getString("APELLIDO"));

                // Crear una instancia de Reporte
                Reporte reporte = new Reporte();
                reporte.setId(resultSet.getInt("ID"));
                reporte.setTitulo(resultSet.getString("TITULO"));
                reporte.setDescripcion(resultSet.getString("DESCRIPCION"));

                // Crear una instancia de EstadoReporte
                EstadoReporte estadoRep = new EstadoReporte();
                estadoRep.setId(resultSet.getInt("ID"));
                estadoRep.setEstado(resultSet.getString("ESTADO"));

                // Crear una instancia de CierreReporte
                leerCierre = new CierreReporte(
                        reporte,
                        usuario,
                        resultSet.getString("MOTIVO_CIERRE"),
                        obtenerImagenDesdeResultSet(resultSet, "CIERRE_IMAGEN"),
                        resultSet.getDate("FECHA_CIERRE"),
                        estadoRep
                );
            } else {
                result2 = "No se encontraron datos para el ID de reporte especificado.";
            }

            con.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            result2 = "Conexion no exitosa";
        }
        return response;
    }
    @Override
    protected void onPostExecute(String response) {
        if(leerCierre != null){
            imagen.setImageBitmap(leerCierre.getImagen());
            motivo.setText(leerCierre.getMotivo());
            atendido.setText("Atendido por " + leerCierre.getUser().getUsername() + " el día " + leerCierre.getFecha_cierre().toString());;
        }
    }

    private Bitmap obtenerImagenDesdeResultSet(ResultSet resultSet, String nombreColumna) {
        try {
            Blob blob = resultSet.getBlob(nombreColumna);
            if (blob != null) {
                int blobLength = (int) blob.length();
                byte[] bytes = blob.getBytes(1, blobLength);
                blob.free();
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
