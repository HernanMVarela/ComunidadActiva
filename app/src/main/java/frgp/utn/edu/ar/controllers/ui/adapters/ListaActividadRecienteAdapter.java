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

public class ListaActividadRecienteAdapter extends ArrayAdapter<Logs> {
    public ListaActividadRecienteAdapter(Context context, List<Logs> listaReportes) {
        super(context, R.layout.layout_list_item_actividad_reciente, listaReportes);
        setDropDownViewResource(R.layout.layout_list_item_actividad_reciente);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Logs log = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_item_actividad_reciente, parent,false);
        }

        TextView titulo = convertView.findViewById(R.id.tvTituloActividad);
        TextView descripcion = convertView.findViewById(R.id.tvDescripcionActividadReciente);
        TextView fecha = convertView.findViewById(R.id.tvFechaActividadReciente);
        assert log != null;

        titulo.setText(log.getAccion().toString());
        descripcion.setText(log.getDescripcion());
        fecha.setText(log.getFecha().toString());
        return convertView;
    }
}
