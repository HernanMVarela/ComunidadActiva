package frgp.utn.edu.ar.controllers.data.remote.reporte;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import frgp.utn.edu.ar.controllers.data.model.ReseniaReporte;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAVerificarUsuarioVoto extends AsyncTask<String, Void, Boolean> {
    private Context context;
    private ReseniaReporte resenia;
    public DMAVerificarUsuarioVoto(ReseniaReporte resenia){
        this.resenia = resenia;
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        Boolean response = false;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String query = "SELECT COUNT(*) FROM RESENIA_REPORTE WHERE ID_REPORTE = ? AND ID_VOTANTE = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, resenia.getReporte().getId());
            ps.setInt(2, resenia.getVotante().getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Si count es mayor que 0, significa que el usuario ya votó - Devuelve TRUE
            }
            ps.close();
            con.close();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
