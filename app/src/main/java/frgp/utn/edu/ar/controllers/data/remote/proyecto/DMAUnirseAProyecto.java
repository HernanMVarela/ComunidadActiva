package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMAUnirseAProyecto extends AsyncTask<String, Void, Boolean> {

    private int idUserN, idProyectoN;
    private int filasafectadas;

    public DMAUnirseAProyecto(int idUser, int idProyecto)
    {
        idUserN=idUser;
        idProyectoN=idProyecto;
    }

    @Override
    protected Boolean doInBackground(String... urls) {

        try {
            filasafectadas = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String insert = "INSERT INTO USERS_PROYECTO (ID_USER, ID_PROYECTO, FECHA_UNION) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = con.prepareStatement(insert);

            preparedStatement.setInt(1,idUserN);
            preparedStatement.setInt(2, idProyectoN);
            preparedStatement.setDate(3, new Date(System.currentTimeMillis()));

            filasafectadas = preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();
            if(filasafectadas!=0){
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