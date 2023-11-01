package frgp.utn.edu.ar.controllers.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.model.Voluntario;

public class ListaVoluntariosAdapter extends ArrayAdapter<Voluntario> {

    public ListaVoluntariosAdapter(Context context, List<Voluntario> listaUsuarios) {
        super(context, R.layout.layout_list_item_voluntario, listaUsuarios);
        setDropDownViewResource(R.layout.layout_list_item_voluntario);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Voluntario user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_item_voluntario, parent,false);
        }

        TextView username = convertView.findViewById(R.id.tv_list_voluntario_username);
        TextView fechaUnion = convertView.findViewById(R.id.tv_list_voluntario_ingreso);
        TextView fechaSalida = convertView.findViewById(R.id.tv_list_voluntario_salida);
        TextView nombreUsuario = convertView.findViewById(R.id.tv_list_voluntario_nombre);
        TextView estado = convertView.findViewById(R.id.tv_list_voluntario_estado);

        assert user != null;
        username.setText(user.getUsername());
        nombreUsuario.setText(user.getNombre() + " " + user.getApellido());
        fechaUnion.setText("Se unió el " + user.getFecha_union().toString());
        if(user.getFecha_salida()!=null){
            fechaSalida.setText("Salió el " + user.getFecha_union().toString());
        }else{
            fechaSalida.setText("Hasta la fecha");
        }
        if(user.isActivo()){
            estado.setText("El usuario está activo en el proyecto");
        }else{
            estado.setText("El usuario abandonó el proyecto");
        }


        return convertView;
    }
}
