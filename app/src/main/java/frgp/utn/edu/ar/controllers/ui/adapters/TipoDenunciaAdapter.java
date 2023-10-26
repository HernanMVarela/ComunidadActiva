package frgp.utn.edu.ar.controllers.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.TipoDenuncia;


public class TipoDenunciaAdapter extends ArrayAdapter<TipoDenuncia> {

    public TipoDenunciaAdapter(Context context, List<TipoDenuncia> tipo) {
        super(context, R.layout.spinner_generico, tipo);
        setDropDownViewResource(R.layout.spinner_generico);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TipoDenuncia tipo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_generico, parent,false);
        }

        TextView textView = convertView.findViewById(R.id.spinTextView);
        assert tipo != null;
        textView.setText(tipo.getTipo());

        return convertView;
    }
}
