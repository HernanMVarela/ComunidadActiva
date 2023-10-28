package frgp.utn.edu.ar.controllers.data.remote.usuario;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;

public class DMACambiarEstadoUsuario extends AsyncTask<String, Void, Boolean> {

    private Usuario usuario;
    private Context context;

    public DMACambiarEstadoUsuario(Usuario usuario, Context context) {
        this.usuario = usuario;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE USUARIOS SET id_estado = ? WHERE id = ?");

            preparedStatement.setInt(1, usuario.getEstado().getId());
            preparedStatement.setInt(2, usuario.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                preparedStatement.close();
                con.close();
                return true;
            } else {
                preparedStatement.close();
                con.close();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean){
            if(usuario.getEstado().getEstado().equals("ELIMINADO")){
                Toast.makeText(context, "USUARIO ELIMINADO", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context, "Usuario modificado", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_LONG).show();
        }
    }
}
