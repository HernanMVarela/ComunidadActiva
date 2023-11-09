package frgp.utn.edu.ar.controllers.data.remote.denuncia;

import android.content.Context;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAActualizarEstadoDenunciaReporte extends AsyncTask<String, Void, Boolean> {
    private Denuncia modificar;
    public DMAActualizarEstadoDenunciaReporte(Denuncia modificar) {
        this.modificar = modificar;
    }
    @Override
    protected Boolean doInBackground(String... strings) {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String query = "UPDATE DENUNCIAS_REPORTES SET ID_ESTADO = ? WHERE ID_REPORTE = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, modificar.getEstado().getId());  // Asigna el nuevo valor del estado (3)
            ps.setInt(2, modificar.getPublicacion().getId()); // ID de la publicación a actualizar
            int dataRowModif = ps.executeUpdate();

            ps.close();
            con.close();
            if(dataRowModif!=0){
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
