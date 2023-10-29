package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAAbandonarProyecto extends AsyncTask<String, Void, String> {

    private final Context context;
    private int idUserN, idProyectoN;
    private int filasafectadas;
    private String resultado;
    private Button boton;

    public DMAAbandonarProyecto(int idUser, int idProyecto, Button botonAUsar, Context ct)
    {
        idUserN=idUser;
        idProyectoN=idProyecto;
        context = ct;
        resultado=" ";
        boton = botonAUsar;
    }

    @Override
    protected String doInBackground(String... urls) {

        try {
            filasafectadas = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            String insert = "UPDATE USERS_PROYECTO SET FECHA_SALIDA = ? WHERE ID_USER = ? AND ID_PROYECTO = ? ;";

            PreparedStatement preparedStatement = con.prepareStatement(insert);

            preparedStatement.setDate(1, new Date(System.currentTimeMillis()));
            preparedStatement.setInt(2,idUserN);
            preparedStatement.setInt(3, idProyectoN);

            filasafectadas = preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();
            resultado = "Y";
        } catch (Exception e) {
            e.printStackTrace();
            resultado = "Fallo al Abandonar";
        }
        return resultado;
    }
    @Override
    protected void onPostExecute(String response) {
        if(filasafectadas!=0){
            boton.setVisibility(View.GONE);
        }else{
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        }
    }
}