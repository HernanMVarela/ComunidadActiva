package frgp.utn.edu.ar.controllers.data.remote.denuncia;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;


public class DMAListarDenuncias extends AsyncTask<String, Void, String> {
    private Context context;
    private ListView listado;
    private static String result2;
    private static List<Denuncia> listaDenuncias;

    public DMAListarDenuncias(ListView listview, Context ct){
        listado = listview;
        context = ct;
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();

            
        }catch (Exception e){
            e.printStackTrace();
            result2 = "Conexion no exitosa";
        }

        return response;
    }
}
