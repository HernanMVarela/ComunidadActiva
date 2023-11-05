package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAAbandonarProyecto extends AsyncTask<String, Void, Boolean> {

    private int idUserN, idProyectoN;

    public DMAAbandonarProyecto(int idUser, int idProyecto)
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
            String insert = "UPDATE USERS_PROYECTO SET FECHA_SALIDA = ? WHERE ID_USER = ? AND ID_PROYECTO = ? ;";

            PreparedStatement preparedStatement = con.prepareStatement(insert);

            preparedStatement.setDate(1, new Date(System.currentTimeMillis()));
            preparedStatement.setInt(2,idUserN);
            preparedStatement.setInt(3, idProyectoN);

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