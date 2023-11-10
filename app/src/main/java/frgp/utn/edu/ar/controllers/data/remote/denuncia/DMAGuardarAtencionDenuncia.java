package frgp.utn.edu.ar.controllers.data.remote.denuncia;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.model.AtencionDenuncia;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAGuardarAtencionDenuncia extends AsyncTask<String, Void, Boolean> {

    private AtencionDenuncia nuevo;
    //Constructor
    public DMAGuardarAtencionDenuncia(AtencionDenuncia nuevo)
    {
        this.nuevo = nuevo;
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String query = "";

            if(nuevo.getTipo().getTipo().equals("REPORTE")){
                query = "INSERT INTO ATENCION_DENUNCIA_REPORTES (ID_REPORTE, ID_USER, FECHA, COMENTARIO) VALUES (?, ?, ?, ?)";
            }else if(nuevo.getTipo().getTipo().equals("PROYECTO")){
                query = "INSERT INTO ATENCION_DENUNCIA_PROYECTO (ID_PROYECTO, ID_USER, FECHA, COMENTARIO) VALUES (?, ?, ?, ?)";
            }
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1,nuevo.getPublicacion().getId());
            ps.setInt(2,nuevo.getModerador().getId());
            ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            ps.setString(4,nuevo.getComentario());

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
