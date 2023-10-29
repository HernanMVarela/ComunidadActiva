package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMABuscarUsuarioEnProyecto extends AsyncTask<String, Void, String> {
    private Context context;
    private ListView listado;
    private static String resultado;
    private boolean controlNoExiste=true, controlNull=false;
    private Button botonN;
    private int idUserN, idProyectoN;
    public DMABuscarUsuarioEnProyecto(int isUserB, int idProyectoB, Button boton, Context ct) {
        context = ct;
        idProyectoN = idProyectoB;
        idUserN=isUserB;
        botonN=boton;
    }
    @Override
    protected String doInBackground(String... urls) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            String query = "SELECT ID_USER, ID_PROYECTO, FECHA_SALIDA FROM USERS_PROYECTO WHERE ID_USER = ? AND ID_PROYECTO = ?;";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1,idUserN);
            preparedStatement.setInt(2,idProyectoN);
            ResultSet rs = preparedStatement.executeQuery();
            resultado = " ";
            while (rs.next()) {
                controlNoExiste=false;
                if(rs.getDate("FECHA_SALIDA")==null){
                    controlNull=true;
                }
            }
            resultado = "Conexion exitosa";
        } catch (Exception e) {
            e.printStackTrace();
            resultado = "Conexion no exitosa";
        }
        return resultado;
    }
    protected void onPostExecute(String response) {
        if(controlNoExiste)
            {botonN.setText("Unirse");}
        else
        {
            if(controlNull)
            {botonN.setText("Abandonar");}
            else
            {botonN.setVisibility(View.GONE);}
        }
    }
}
