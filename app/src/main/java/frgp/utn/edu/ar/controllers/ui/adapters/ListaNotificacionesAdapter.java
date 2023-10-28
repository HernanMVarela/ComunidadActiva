package frgp.utn.edu.ar.controllers.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Logs;
import frgp.utn.edu.ar.controllers.data.model.Notificacion;

public class ListaNotificacionesAdapter extends ArrayAdapter<Notificacion> {
    public ListaNotificacionesAdapter(Context context, List<Notificacion> listaNotificaciones) {
        super(context, R.layout.layout_list_notificaciones, listaNotificaciones);
        setDropDownViewResource(R.layout.layout_list_notificaciones);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Notificacion log = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_notificaciones, parent,false);
        }

        TextView descripcion = convertView.findViewById(R.id.tvDescripcionNotificacion);
        TextView fecha = convertView.findViewById(R.id.tvFechaNotificacion);
        assert log != null;

        descripcion.setText(log.getDescripcion());
        fecha.setText(log.getFecha().toString());
        return convertView;
    }
}
