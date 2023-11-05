package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAReUnirseProyecto extends AsyncTask<String, Void, Boolean> {

    private int idUserN, idProyectoN;
    public DMAReUnirseProyecto(int idUser, int idProyecto)
    {
        idUserN=idUser;
        idProyectoN=idProyecto;
    }
    @Override
    protected Boolean doInBackground(String... urls) {

        try {
            int filasafectadas = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String insert = "UPDATE USERS_PROYECTO SET FECHA_UNION = ?, FECHA_SALIDA = ? WHERE ID_USER = ? AND ID_PROYECTO = ? ;";

            PreparedStatement preparedStatement = con.prepareStatement(insert);

            preparedStatement.setDate(1, new Date(System.currentTimeMillis()));
            preparedStatement.setDate(2,null);
            preparedStatement.setInt(3,idUserN);
            preparedStatement.setInt(4, idProyectoN);

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