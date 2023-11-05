package frgp.utn.edu.ar.controllers.data.remote.reporte;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.model.CierreReporte;
import frgp.utn.edu.ar.controllers.data.model.EstadoReporte;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAGuardarCierreReporte extends AsyncTask<String, Void, String> {

    private Context context;
    private CierreReporte nuevo;
    private static String result2;
    private int dataRowModif;

    //Constructor
    public DMAGuardarCierreReporte(CierreReporte nuevo, Context ct)
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

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            nuevo.getImagen().compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            String query = "INSERT INTO CIERRES_REPORTE (ID_REPORTE ,ID_USER ,MOTIVO_CIERRE ,FECHA_CIERRE ,IMAGEN,ID_ESTADOCIERRE) VALUES (?,?,?,?,?,?);";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1,nuevo.getReporte().getId());
            ps.setInt(2,nuevo.getUser().getId());
            ps.setString(3,nuevo.getMotivo());
            ps.setDate(4, new java.sql.Date(nuevo.getFechaCierreAsDate().getTime()));
            ps.setBytes(5, byteArray); // Guardar la imagen como un array de bytes
            ps.setInt(6, nuevo.getEstado().getId());

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
            nuevo.getReporte().setEstado(new EstadoReporte(2,"PENDIENTE"));
            DMAActualizarEstadoReporte dmaActualizar = new DMAActualizarEstadoReporte(nuevo.getReporte(),context);
            dmaActualizar.execute();
            Toast.makeText(context, "Reporte atendido!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "No se pudo crear la solicitud", Toast.LENGTH_SHORT).show();
        }
    }
}
