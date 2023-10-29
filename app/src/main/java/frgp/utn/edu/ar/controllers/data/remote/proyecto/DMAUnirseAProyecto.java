package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Date;

import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAUnirseAProyecto extends AsyncTask<String, Void, String> {

    private final Context context;
    private int idUserN, idProyectoN;
    private int filasafectadas;
    private String resultado;
    private Button boton;

    public DMAUnirseAProyecto(int idUser, int idProyecto, Button botonAUsar, Context ct)
    {
        idUserN=idUser;
        idProyectoN=idProyecto;
        context = ct;
        resultado=" ";
        boton=botonAUsar;
    }

    @Override
    protected String doInBackground(String... urls) {

        try {
            filasafectadas = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            String insert = "INSERT INTO USERS_PROYECTO (ID_USER, ID_PROYECTO, FECHA_UNION) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = con.prepareStatement(insert);

            preparedStatement.setInt(1,idUserN);
            preparedStatement.setInt(2, idProyectoN);
            preparedStatement.setDate(3, new Date(System.currentTimeMillis()));

            filasafectadas = preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();
            resultado = "X";
        } catch (Exception e) {
            e.printStackTrace();
            resultado = "Fallo al Unirse";
        }
        return resultado;
    }
    @Override
    protected void onPostExecute(String response) {
        if(filasafectadas!=0){
            boton.setText("Abandonar");
        }else{
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        }
    }
}