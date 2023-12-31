package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMANuevoProyecto  extends AsyncTask<String, Void, Boolean> {

    private Proyecto nuevo;

    public DMANuevoProyecto(Proyecto nuevo)
    {
        this.nuevo = nuevo;
    }

    @Override
    protected Boolean doInBackground(String... urls) {

        try {
            int filasafectadas = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String insert = "INSERT INTO PROYECTOS (TITULO, DESCRIPCION, LATITUD, LONGITUD, CUPO, ID_USER, ID_TIPO, ID_ESTADO, CONTACTO, AYUDA_ESPECIFICA, FECHA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);";

            PreparedStatement preparedStatement = con.prepareStatement(insert);

            preparedStatement.setString(1, nuevo.getTitulo());
            preparedStatement.setString(2, nuevo.getDescripcion());
            preparedStatement.setDouble(3, nuevo.getLatitud());
            preparedStatement.setDouble(4, nuevo.getLongitud());
            preparedStatement.setInt(5, nuevo.getCupo()+1);
            preparedStatement.setInt(6, nuevo.getOwner().getId());
            preparedStatement.setInt(7, nuevo.getTipo().getId());
            preparedStatement.setInt(8, nuevo.getEstado().getId());
            preparedStatement.setString(9, nuevo.getContacto());
            preparedStatement.setString(10, nuevo.getRequerimientos());
            preparedStatement.setDate(11, new java.sql.Date(nuevo.getFecha().getTime()));

            filasafectadas = preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();
            if(filasafectadas !=0){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}