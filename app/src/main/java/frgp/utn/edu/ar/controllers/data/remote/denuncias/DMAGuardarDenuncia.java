package frgp.utn.edu.ar.controllers.data.remote.denuncias;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import frgp.utn.edu.ar.controllers.data.model.CierreReporte;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAGuardarDenuncia extends AsyncTask<String, Void, String> {

    private Context context;
    private Denuncia nuevo;
    private static String result2;
    private int dataRowModif;

    //Constructor
    public DMAGuardarDenuncia(Denuncia nuevo, Context ct)
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

            String query = "INSERT INTO DENUNCIAS (ID_PUBLICACION ,ID_TIPO ,ID_USER ,ID_ESTADO ,TITULO ,DESCRIPCION, FECHA_CREACION) VALUES (?,?,?,?,?,?,?);";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1,nuevo.getPublicacion().getId());
            ps.setInt(3,nuevo.getTipo().getId());
            ps.setInt(2,nuevo.getDenunciante().getId());
            ps.setInt(4,nuevo.getEstado().getId());
            ps.setString(5, nuevo.getTitulo());
            ps.setString(6,nuevo.getDescripcion());
            ps.setDate(7, new java.sql.Date(nuevo.getFecha_creacion().getTime()));

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
            Toast.makeText(context, "Denuncia guardada", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "No se pudo crear la denuncia", Toast.LENGTH_SHORT).show();
        }

    }
}
