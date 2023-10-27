package frgp.utn.edu.ar.controllers.ui.adapters;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.Usuario;

public class ListaUsuariosAdapter extends ArrayAdapter<Usuario> {


    public ListaUsuariosAdapter(Context context, List<Usuario> listaUsuarios) {
        super(context, R.layout.layout_list_item_usuario, listaUsuarios);
        setDropDownViewResource(R.layout.layout_list_item_usuario);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Usuario user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_item_usuario, parent,false);
        }

        TextView username = convertView.findViewById(R.id.tvTituloListaUsuario);
        TextView fechaInicio = convertView.findViewById(R.id.tvtFechaRegistro);
        TextView correo = convertView.findViewById(R.id.tvCorreoUser);
        TextView tipo = convertView.findViewById(R.id.tvTipoCuentaUser);
        TextView estado = convertView.findViewById(R.id.tvEstadoUser);

        assert user != null;
        username.setText(user.getUsername());
        fechaInicio.setText(user.getFecha_alta().toString());
        correo.setText(user.getCorreo());
        tipo.setText(user.getTipo().getTipo());
        estado.setText("Estado: " + user.getEstado().getEstado());

        return convertView;
    }
}
