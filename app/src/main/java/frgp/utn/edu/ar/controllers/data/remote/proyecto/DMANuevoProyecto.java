package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;

public class DMANuevoProyecto  extends AsyncTask<String, Void, String> {

    private final Context context;
    private Proyecto nuevo;
    private int filasafectadas;
    private String resultado;

    public DMANuevoProyecto(Proyecto nuevo, Context ct)
    {
        this.nuevo = nuevo;
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
            String insert = "INSERT INTO PROYECTOS (TITULO, DESCRIPCION, LATITUD, LONGITUD, CUPO, ID_USER, ID_TIPO, ID_ESTADO, CONTACTO, AYUDA_ESPECIFICA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement preparedStatement = con.prepareStatement(insert);

            preparedStatement.setString(1, nuevo.getTitulo());
            preparedStatement.setString(2, nuevo.getDescripcion());
            preparedStatement.setDouble(3, nuevo.getLatitud());
            preparedStatement.setDouble(4, nuevo.getLongitud());
            preparedStatement.setInt(5, nuevo.getCupo());
            preparedStatement.setInt(6, 1);
            preparedStatement.setInt(7, nuevo.getTipo().getId());
            preparedStatement.setInt(8, nuevo.getEstado().getId());
            preparedStatement.setString(9, nuevo.getContacto());
            preparedStatement.setString(10, nuevo.getRequerimientos());

            filasafectadas = preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();
            resultado = " ";
        } catch (Exception e) {
            e.printStackTrace();
            resultado = "Fallo de Insert";
        }
        return resultado;
    }
    @Override
    protected void onPostExecute(String response) {
        if(filasafectadas!=0){
            Toast.makeText(context, "Proyecto Creado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        }
    }
}