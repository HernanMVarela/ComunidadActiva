package frgp.utn.edu.ar.controllers.data.remote.denuncias;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.EstadoReporte;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.data.remote.reporte.DMAActualizarEstadoReporte;

public class DMAGuardarDenunciaReporte extends AsyncTask<String, Void, Boolean> {

    private Denuncia nuevo;

    //Constructor
    public DMAGuardarDenunciaReporte(Denuncia nuevo)
    {
        this.nuevo = nuevo;
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String query = "INSERT INTO DENUNCIAS_REPORTES (ID_REPORTE ,ID_USER ,ID_ESTADO ,TITULO ,DESCRIPCION, FECHA_CREACION) VALUES (?,?,?,?,?,?);";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1,nuevo.getPublicacion().getId());
            ps.setInt(2,nuevo.getDenunciante().getId());
            ps.setInt(3,nuevo.getEstado().getId());
            ps.setString(4, nuevo.getTitulo());
            ps.setString(5,nuevo.getDescripcion());
            ps.setDate(6, new java.sql.Date(nuevo.getFecha_creacion().getTime()));

            int dataRowModif = ps.executeUpdate();
            ps.close();
            con.close();
            if(dataRowModif !=0){
                return true;
            }
            return false;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
