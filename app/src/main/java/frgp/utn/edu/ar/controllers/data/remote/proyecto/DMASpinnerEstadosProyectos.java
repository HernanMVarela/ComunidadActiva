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

import frgp.utn.edu.ar.controllers.data.model.EstadoProyecto;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.ui.adapters.EstadoProyectoAdapter;

public class DMASpinnerEstadosProyectos extends AsyncTask<String, Void, String> {

    private Context context;
    private Spinner spinEstadosProyectos;
    private static String result2;
    private static List<EstadoProyecto> listaEstadosProyectos;

    //Constructor
    public DMASpinnerEstadosProyectos(Spinner spin, Context ct)
    {
        spinEstadosProyectos = spin;
        context = ct;
    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ESTADOS_PROYECTO");
            result2 = " ";
            listaEstadosProyectos = new ArrayList<EstadoProyecto>();
            while(rs.next()) {
                EstadoProyecto categoria = new EstadoProyecto();
                categoria.setId(rs.getInt("ID"));
                categoria.setEstado(rs.getString("ESTADO"));

                listaEstadosProyectos.add(categoria);
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
        EstadoProyectoAdapter adapter = new EstadoProyectoAdapter(context, listaEstadosProyectos);
        spinEstadosProyectos.setAdapter(adapter);
    }
}
