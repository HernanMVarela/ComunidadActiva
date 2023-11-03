package frgp.utn.edu.ar.controllers.data.remote.reporte;

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
import frgp.utn.edu.ar.controllers.data.model.ReseniaReporte;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAGuardarResenia extends AsyncTask<String, Void, Boolean> {

    private final ReseniaReporte nuevo;

    //Constructor
    public DMAGuardarResenia(ReseniaReporte nuevo)
    {
        this.nuevo = nuevo;
    }

    @Override
    protected Boolean doInBackground(String... urls) {
         try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();

            String query = "INSERT INTO RESENIA_REPORTE (ID_REPORTE ,ID_VOTANTE ,PUNTAJE) VALUES (?,?,?);";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1,nuevo.getReporte().getId());
            ps.setInt(2,nuevo.getVotante().getId());
            ps.setFloat(3,nuevo.getPuntaje());

            int dataRowModif = ps.executeUpdate();
            return dataRowModif != 0;
         }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
