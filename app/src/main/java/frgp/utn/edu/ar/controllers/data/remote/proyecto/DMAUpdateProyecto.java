package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAUpdateProyecto extends AsyncTask<String, Void, String> {

    private final Context context;
    private int idProyectoN, idEstadoN;
    private int filasafectadas;
    private String resultado;

    public DMAUpdateProyecto(int idProyecto, int idEstado, Context ct)
    {
        idProyectoN = idProyecto;
        idEstadoN = idEstado;
        context = ct;
        resultado=" ";
    }

    @Override
    protected String doInBackground(String... urls) {

        try {
            filasafectadas = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            Statement st = con.createStatement();
            String insert = "UPDATE PROYECTOS SET ID_ESTADO = ? WHERE ID = ? ;";

            PreparedStatement preparedStatement = con.prepareStatement(insert);

            preparedStatement.setInt(1, idEstadoN);
            preparedStatement.setInt(2, idProyectoN);

            filasafectadas = preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();
            resultado = " ";
        } catch (Exception e) {
            e.printStackTrace();
            resultado = "Fallo de Update";
        }
        return resultado;
    }
    @Override
    protected void onPostExecute(String response) {
        if(filasafectadas!=0){
            Toast.makeText(context, "Actualización Exitosa", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        }
    }
}