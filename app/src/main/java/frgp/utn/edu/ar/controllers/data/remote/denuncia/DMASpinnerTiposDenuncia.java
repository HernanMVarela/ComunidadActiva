package frgp.utn.edu.ar.controllers.data.remote.denuncia;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.TipoDenuncia;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.ui.adapters.TipoDenunciaAdapter;
import frgp.utn.edu.ar.controllers.ui.adapters.TipoReporteAdapter;


public class DMASpinnerTiposDenuncia extends AsyncTask<String, Void, String> {

    private Context context;
    private Spinner spinTipoDenuncia;
    private static String result2;
    private static List<TipoDenuncia> listaTiposDenuncia;

    public DMASpinnerTiposDenuncia(Spinner spin, Context ct)
    {
        spinTipoDenuncia = spin;
        context = ct;
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM TIPOS_DENUNCIA");
            result2 = " ";
            listaTiposDenuncia = new ArrayList<TipoDenuncia>();
            while(rs.next()) {
                TipoDenuncia categoria = new TipoDenuncia();
                categoria.setId(rs.getInt("ID"));
                categoria.setTipo(rs.getString("TIPO"));

                listaTiposDenuncia.add(categoria);
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
        TipoDenunciaAdapter adapter = new TipoDenunciaAdapter(context, listaTiposDenuncia);
        spinTipoDenuncia.setAdapter(adapter);
    }


}
