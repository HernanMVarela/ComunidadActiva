package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.TipoProyecto;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.ui.adapters.TipoProyectoAdapter;

public class DMASpinnerTiposProyectos extends AsyncTask<String, Void, String> {

    private Context context;
    private Spinner spinTipoProyectos;
    private static String result2;
    private static List<TipoProyecto> listaTiposProyectos;

    //Constructor
    public DMASpinnerTiposProyectos(Spinner spin, Context ct)
    {
        spinTipoProyectos = spin;
        context = ct;
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM TIPOS_PROYECTO");
            result2 = " ";
            listaTiposProyectos = new ArrayList<TipoProyecto>();
            while(rs.next()) {
                TipoProyecto categoria = new TipoProyecto();
                categoria.setId(rs.getInt("ID"));
                categoria.setTipo(rs.getString("TIPO"));

                listaTiposProyectos.add(categoria);
            }
            response = "Conexion exitosa";
        }
        catch(Exception e) {
            e.printStackTrace();
            result2 = "Conexion no exitosa";
        }
        return response;

    }
    @Override
    protected void onPostExecute(String response) {
        TipoProyectoAdapter adapter = new TipoProyectoAdapter(context, listaTiposProyectos);
        spinTipoProyectos.setAdapter(adapter);
    }
}
