package frgp.utn.edu.ar.controllers.data.remote.denuncia;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;


public class DMACerrarDenuncia extends AsyncTask<String, Void, String> {

    private Context context;
    private Denuncia modificar;
    private static String result2;
    private int dataRowModif;
    private String tipo;

    public DMACerrarDenuncia(Context context, Denuncia modificar, String tipo) {
        this.context = context;
        this.modificar = modificar;
        this.tipo = tipo;
    }

    @Override
    protected String doInBackground(String... strings) {

        String response = "";
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String query = "";
            //  QUERY CON EL ID DE LA PUBLICACION, PUEDE SER REPORTE O PROYECTO
            if(tipo.equals("REPORTE")){
                query = "UPDATE DENUNCIAS_REPORTES SET ID_ESTADO = ? WHERE ID_REPORTE = ?";
            }
            if(tipo.equals("PROYECTO")){
                query = "UPDATE DENUNCIAS_PROYECTOS SET ID_ESTADO = ? WHERE ID_PROYECTO = ?";
            }
            // Define la consulta SQL de actualización

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, 3);  // Asigna el nuevo valor del estado (3)
            ps.setInt(2, modificar.getPublicacion().getId()); // ID de la publicación a actualizar
            dataRowModif = ps.executeUpdate();

            response = "Éxito"; // La actualización se realizó con éxito

        } catch (Exception e) {
            e.printStackTrace();
            response = "ERROR";
        }finally {
            try {
                if (con != null) {
                    con.close(); // Cierra la conexión a la base de datos en cualquier caso
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        if(dataRowModif==0){
            Toast.makeText(context, "No se pudo CERRAR la Denuncia", Toast.LENGTH_SHORT).show();
        }
    }
}
