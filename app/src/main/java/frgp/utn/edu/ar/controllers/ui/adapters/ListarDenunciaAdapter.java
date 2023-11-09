package frgp.utn.edu.ar.controllers.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.List;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Denuncia;
import frgp.utn.edu.ar.controllers.data.model.DenunciaNuevo;

public class ListarDenunciaAdapter extends ArrayAdapter<Denuncia> {

    public ListarDenunciaAdapter(Context context, List<Denuncia> listaDenuncia) {
        super(context, R.layout.layout_lista_denuncia, listaDenuncia);
        setDropDownViewResource(R.layout.layout_lista_denuncia);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Denuncia denuncia = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_lista_denuncia, parent,false);
        }

        TextView titulo = convertView.findViewById(R.id.tvTituloListaDenuncia);
        TextView usernamefecha = convertView.findViewById(R.id.tvUsernameFechaListaDenuncia);
        CardView cardview = convertView.findViewById(R.id.cardview_lista_denuncia);
        assert denuncia != null;

        titulo.setText(denuncia.getTitulo());

        usernamefecha.setText("Denunciado por " + denuncia.getDenunciante().getUsername() + " el " + denuncia.getFecha_creacion().toString());

        if(denuncia.getEstado().getEstado().equals("CERRADA") || denuncia.getEstado().getEstado().equals("CANCELADA")){
            cardview.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.green_200));
        }
        if(denuncia.getEstado().getEstado().equals("PENDIENTE")){
            cardview.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.danger));
        }
        if(denuncia.getEstado().getEstado().equals("ATENDIDA")){
            cardview.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.warning));
        }
        return convertView;
    }
}
