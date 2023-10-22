package frgp.utn.edu.ar.controllers.data.remote.reporte;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.TipoReporte;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.ui.adapters.TipoReporteAdapter;

public class DMAGuardarReporte extends AsyncTask<String, Void, String> {

    private Context context;
    private Reporte nuevo;
    private static String result2;
    private int dataRowModif;

    //Constructor
    public DMAGuardarReporte(Reporte nuevo, Context ct)
    {
        this.nuevo = nuevo;
        this.context = ct;
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            nuevo.getImagen().compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            String query = "INSERT INTO REPORTES (TITULO,DESCRIPCION,LATITUD,LONGITUD,IMAGEN,FECHA,CANT_VOTOS,PUNTAJE,ID_USER,ID_TIPO,ID_ESTADO) VALUES (?,?,?,?,?,?,?,?,?,?,?);";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1,nuevo.getTitulo());
            ps.setString(2,nuevo.getDescripcion());
            ps.setDouble(3,nuevo.getLatitud());
            ps.setDouble(4,nuevo.getLongitud());
            ps.setBytes(5, byteArray); // Guardar la imagen como un array de bytes
            ps.setDate(6, new java.sql.Date(nuevo.getFecha().getTime()));
            ps.setInt(7, 1);
            ps.setInt(8, 5);
            ps.setInt(9, 1);
            ps.setInt(10, nuevo.getTipo().getId());
            ps.setInt(11, nuevo.getEstado().getId());

            dataRowModif = ps.executeUpdate();
            result2 = " ";

        }
        catch(Exception e) {
            e.printStackTrace();
            result2 = "Conexion no exitosa";
        }
        return response;

    }
    @Override
    protected void onPostExecute(String response) {
        if(dataRowModif!=0){
            Toast.makeText(context, "Reporte creado exitosamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "No se pudo guardar el reporte", Toast.LENGTH_SHORT).show();
        }
    }
}
