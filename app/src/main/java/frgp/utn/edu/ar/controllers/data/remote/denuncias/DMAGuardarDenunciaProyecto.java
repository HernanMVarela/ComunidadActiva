package frgp.utn.edu.ar.controllers.data.remote.denuncias;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.data.remote.proyecto.DMAUpdateProyecto;

public class DMAGuardarDenunciaProyecto extends AsyncTask<String, Void, String> {

    private Context context;
    private static String result2, tituloDenuncia, descripcionDenuncia;
    private int dataRowModif, idProyectoDenuncia, idUserDenuncia;

    //Constructor
    public DMAGuardarDenunciaProyecto(int idProyecto, int idUser, String titulo, String descripcion, Context ct)
    {
        this.idProyectoDenuncia = idProyecto;
        this.idUserDenuncia = idUser;
        this.tituloDenuncia = titulo;
        this.descripcionDenuncia = descripcion;
        this.context = ct;
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String query = "INSERT INTO DENUNCIAS_PROYECTOS (ID_PROYECTO ,ID_USER ,ID_ESTADO ,TITULO ,DESCRIPCION, FECHA_CREACION) VALUES (?,?,?,?,?,?);";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1,idProyectoDenuncia);
            ps.setInt(2,idUserDenuncia);
            ps.setInt(3,2);
            ps.setString(4, tituloDenuncia);
            ps.setString(5,descripcionDenuncia);
            ps.setDate(6, new Date(System.currentTimeMillis()));
            dataRowModif = ps.executeUpdate();
            result2 = " ";
            ps.close();
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
        if(dataRowModif!=0){
            Toast.makeText(context, "Denuncia generada", Toast.LENGTH_SHORT).show();
            DMAUpdateProyecto DMAestado = new DMAUpdateProyecto(idProyectoDenuncia, 5,context);
            DMAestado.execute();
        }else{
            Toast.makeText(context, "No se generó la denuncia.", Toast.LENGTH_SHORT).show();
        }

    }
}
