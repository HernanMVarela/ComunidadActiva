package frgp.utn.edu.ar.controllers.ui.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Reporte;
import frgp.utn.edu.ar.controllers.data.model.TipoReporte;

public class ListaReportesAdapter extends ArrayAdapter<Reporte> {
    private LatLng ubicacion;
    public ListaReportesAdapter(Context context, List<Reporte> listaReportes, LatLng ubicacion) {
        super(context, R.layout.layout_list_item_reporte, listaReportes);
        setDropDownViewResource(R.layout.layout_list_item_reporte);
        this.ubicacion = ubicacion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Reporte reporte = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_item_reporte, parent,false);
        }

        TextView titulo = convertView.findViewById(R.id.tvTituloListaReporte);
        TextView rating = convertView.findViewById(R.id.tvtRatingListaReporte);
        TextView usernamefecha = convertView.findViewById(R.id.tvUsernameFechaListaReporte);
        TextView distancia = convertView.findViewById(R.id.tvDistanciaListaReporte);
        CardView cardview = convertView.findViewById(R.id.cardview_lista_reporte);
        assert reporte != null;
        String puntosFormateados = "0";
        if(reporte.getCant_votos() > 0){
            float puntos = reporte.getPuntaje() / reporte.getCant_votos();
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            puntosFormateados = decimalFormat.format(puntos);
        }
        titulo.setText(reporte.getTitulo());
        rating.setText(puntosFormateados);

        assert ubicacion != null;
        if(reporte.getLongitud() != 0.0 && reporte.getLatitud() != 0.0 && ubicacion.longitude != 0.0 && ubicacion.latitude != 0.0){
            Location ubReporte = new Location("point1");
            ubReporte.setLatitude(reporte.getLatitud());
            ubReporte.setLongitude(reporte.getLongitud());
            Location ubActual = new Location("point2");
            ubActual.setLatitude(ubicacion.latitude);
            ubActual.setLongitude(ubicacion.longitude);
            float distanciaEnKm = ubActual.distanceTo(ubReporte) / 1000;
            String distanciaFormateada = String.format(Locale.ENGLISH,"%.1f Km", distanciaEnKm);
            distancia.setText(distanciaFormateada);
        }else{
            distancia.setText("--Km");
        }
        usernamefecha.setText("Publicado por " + reporte.getOwner().getUsername() + " el " + reporte.getFecha().toString());

        if(reporte.getEstado().getEstado().equals("DENUNCIADO")){
            cardview.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.danger));
        } else if (reporte.getEstado().getEstado().equals("CERRADO")){
            cardview.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.closed));
        } else if (reporte.getEstado().getEstado().equals("ATENDIDO")){
            cardview.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.teal_700));
        }
        return convertView;
    }
}
