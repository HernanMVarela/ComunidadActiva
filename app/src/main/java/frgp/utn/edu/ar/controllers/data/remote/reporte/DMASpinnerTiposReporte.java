package frgp.utn.edu.ar.controllers.data.remote.reporte;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.TipoReporte;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.ui.adapters.TipoReporteAdapter;

public class DMASpinnerTiposReporte extends AsyncTask<String, Void, String> {

    private Context context;
    private Spinner spinTipoReporte;
    private int selected;
    private static List<TipoReporte> listaTiposReporte;

    //Constructor
    public DMASpinnerTiposReporte(Spinner spin, Context ct, int selected)
    {
        spinTipoReporte = spin;
        this.selected = selected;
        context = ct;
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM TIPOS_REPORTE");
            listaTiposReporte = new ArrayList<TipoReporte>();
            while(rs.next()) {
                TipoReporte categoria = new TipoReporte();
                categoria.setId(rs.getInt("ID"));
                categoria.setTipo(rs.getString("TIPO"));

                listaTiposReporte.add(categoria);
            }
            response = "Conexion exitosa";
            rs.close();
            st.close();
            con.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return response;

    }
    @Override
    protected void onPostExecute(String response) {
        TipoReporteAdapter adapter = new TipoReporteAdapter(context, listaTiposReporte);
        spinTipoReporte.setAdapter(adapter);
        spinTipoReporte.setSelection(selected);
    }
}
