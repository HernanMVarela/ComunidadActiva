package frgp.utn.edu.ar.controllers.data.remote.proyecto;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import frgp.utn.edu.ar.controllers.data.model.EstadoProyecto;
import frgp.utn.edu.ar.controllers.data.model.EstadoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Proyecto;
import frgp.utn.edu.ar.controllers.data.model.TipoProyecto;
import frgp.utn.edu.ar.controllers.data.model.TipoUsuario;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.DataDB;
import frgp.utn.edu.ar.controllers.ui.adapters.ListaProyectosAdapter;

public class DMAListviewProyectos extends AsyncTask<String, Void, String> {
    private Context context;
    private ListView listado;
    private static String resultado;
    private static List<Proyecto> listaDeProyectos;
    private int IdTipoProyecto = 1;
    private int IdEstadoProyecto= 1;
    private int filtroEstado=0;
    private int filtroTipo=0;
    private String nombreProyectoBuscado="";
    public DMAListviewProyectos(ListView listview, Context ct,int usarT,int usarE, int idTipoProyecto, int idEstadoProyecto, String nombuscado) {
        listado = listview;
        context = ct;
        IdTipoProyecto = idTipoProyecto;
        IdEstadoProyecto = idEstadoProyecto;
        nombreProyectoBuscado = nombuscado;
        filtroEstado=usarE;
        filtroTipo=usarT;
    }
    @Override
    protected String doInBackground(String... urls) {
        listaDeProyectos = new ArrayList<Proyecto>();
        boolean filtrado = false;
        int contador=0, ordenT=0, ordenE=0,ordenN=0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
            String query = "SELECT P.ID, P.TITULO, P.DESCRIPCION, P.LATITUD, P.LONGITUD, P.CUPO, P.ID_USER, P.ID_TIPO AS PROYECTO_ID_TIPO, P.ID_ESTADO AS PROYECTO_ID_ESTADO, P.CONTACTO, P.AYUDA_ESPECIFICA, P.FECHA_CREACION, " +
                            "U.USERNAME, U.PUNTUACION, U.NOMBRE, U.APELLIDO, U.TELEFONO, U.CORREO, U.FECHA_NAC, U.CREACION, U.ID_ESTADO AS USUARIO_ID_ESTADO, U.ID_TIPO AS USUARIO_ID_TIPO, " +
                            "TP.TIPO AS TIPO_PROYECTO, EP.ESTADO AS ESTADO_PROYECTO, " +
                            "UT.TIPO AS TIPO_USUARIO, EU.ESTADO AS ESTADO_USUARIO " +
                            "FROM PROYECTOS AS P " +
                            "INNER JOIN USUARIOS AS U ON P.ID_USER = U.ID " +
                            "INNER JOIN TIPOS_PROYECTO AS TP ON P.ID_TIPO = TP.ID " +
                            "INNER JOIN ESTADOS_PROYECTO AS EP ON P.ID_ESTADO = EP.ID " +
                            "INNER JOIN TIPOS_USUARIO AS UT ON U.ID_TIPO = UT.ID " +
                            "INNER JOIN ESTADOS_USUARIO AS EU ON U.ID_ESTADO = EU.ID";
            if(filtroTipo==1){
                contador+=1;
                ordenT=contador;
                if(!filtrado){
                    query = query + " WHERE P.ID_TIPO = ?";
                    filtrado=true;
                }
                else{
                    query = query + " AND P.ID_TIPO = ?";
                }
            }
            if(filtroEstado==1){
                contador+=1;
                ordenE=contador;
                if(!filtrado){
                    query = query + " WHERE P.ID_ESTADO = ?";
                    filtrado=true;
                }
                else{
                    query = query + " AND P.ID_ESTADO = ?";
                }
            }
            if (!nombreProyectoBuscado.trim().isEmpty()) {
                contador+=1;
                ordenN=contador;
                if(!filtrado){
                    query = query + " WHERE P.TITULO LIKE ?";
                    filtrado=true;
                }
                else{
                    query = query + " AND P.TITULO LIKE ?";
                }
            }
            query = query + " ORDER BY ID DESC";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            if(filtroTipo==1){
                preparedStatement.setInt(ordenT, IdTipoProyecto);
            }
            if(filtroEstado==1){
                preparedStatement.setInt(ordenE, IdEstadoProyecto);
            }
            if (!nombreProyectoBuscado.trim().isEmpty()) {
                preparedStatement.setString(ordenN, "%" + nombreProyectoBuscado + "%");
            }
            ResultSet rs = preparedStatement.executeQuery();
            resultado = " ";
            while (rs.next()) {
                // Crea objetos TipoProyecto y EstadoProyecto
                TipoProyecto tipoProyecto = new TipoProyecto(rs.getInt("PROYECTO_ID_TIPO"), rs.getString("TIPO_PROYECTO"));
                EstadoProyecto estadoProyecto = new EstadoProyecto(rs.getInt("PROYECTO_ID_ESTADO"), rs.getString("ESTADO_PROYECTO"));

                // Crea objetos TipoUsuario y EstadoUsuario para el usuario
                TipoUsuario tipoUsuario = new TipoUsuario(rs.getInt("USUARIO_ID_TIPO"), rs.getString("TIPO_USUARIO"));
                EstadoUsuario estadoUsuario = new EstadoUsuario(rs.getInt("USUARIO_ID_ESTADO"), rs.getString("ESTADO_USUARIO"));

                // Crea el objeto Usuario y asigna los valores
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("ID_USER"));
                usuario.setUsername(rs.getString("USERNAME"));
                usuario.setPuntuacion(rs.getInt("PUNTUACION"));
                usuario.setNombre(rs.getString("NOMBRE"));
                usuario.setApellido(rs.getString("APELLIDO"));
                usuario.setTelefono(rs.getString("TELEFONO"));
                usuario.setCorreo(rs.getString("CORREO"));
                usuario.setFecha_nac(rs.getDate("FECHA_NAC"));
                usuario.setFecha_alta(rs.getDate("CREACION"));
                usuario.setEstado(estadoUsuario);
                usuario.setTipo(tipoUsuario);

                // Crea el objeto Proyecto y asigna los valores
                Proyecto proyecto = new Proyecto();
                proyecto.setId(rs.getInt("ID"));
                proyecto.setTitulo(rs.getString("TITULO"));
                proyecto.setDescripcion(rs.getString("DESCRIPCION"));
                proyecto.setLatitud(rs.getDouble("LATITUD"));
                proyecto.setLongitud(rs.getDouble("LONGITUD"));
                proyecto.setCupo(rs.getInt("CUPO"));
                proyecto.setOwner(usuario);
                proyecto.setTipo(tipoProyecto);
                proyecto.setEstado(estadoProyecto);
                proyecto.setContacto(rs.getString("CONTACTO"));
                proyecto.setRequerimientos(rs.getString("AYUDA_ESPECIFICA"));
                proyecto.setFecha(rs.getDate("FECHA_CREACION"));

                listaDeProyectos.add(proyecto);
            }
            rs.close();
            con.close();
            resultado = "Conexion exitosa";
        } catch (Exception e) {
            e.printStackTrace();
            resultado = "Conexion no exitosa";
        }
        return resultado;
    }
    protected void onPostExecute(String response) {
        try{
        if(listaDeProyectos.isEmpty()) {
            resultado = "Sin Resultados";
            ListaProyectosAdapter adapter = new ListaProyectosAdapter(context, listaDeProyectos);
            listado.setAdapter(adapter);
                }
        else {
            ListaProyectosAdapter adapter = new ListaProyectosAdapter(context, listaDeProyectos);
            listado.setAdapter(adapter);
        }
             }
        catch (Error e){
            Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
        }
    }
}
