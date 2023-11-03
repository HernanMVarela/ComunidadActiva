package frgp.utn.edu.ar.controllers.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.ui.viewmodels.CrearInformeViewModel;


public class CrearInformeModeradorFragment extends Fragment {


    private CrearInformeViewModel mViewModel;
    private EditText etFechaInicio, etFechaFin;
    private ImageButton btnFechaInicio, btnFechaFin;
    private DatePicker dpFechaInicio, dpFechaFin;
    private Spinner spTipoInforme;
    private TextView tvTipoInformeModerador;


    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_crear_informe_moderador, container, false);

        //vinculo variables a objetos del view
        etFechaInicio = (EditText) view.findViewById(R.id.editTextFechaInicioModerador);
        etFechaFin = (EditText) view.findViewById(R.id.editTextFechaFinModerador);
        dpFechaInicio = (DatePicker) view.findViewById(R.id.dpFechaInicioModerador);
        dpFechaFin = (DatePicker) view.findViewById(R.id.dpFechaFinModerador);
        btnFechaInicio = (ImageButton) view.findViewById(R.id.btnFechaInicioModerador);
        btnFechaFin = (ImageButton) view.findViewById(R.id.btnFechaFinModerador);
        spTipoInforme = (Spinner) view.findViewById(R.id.spTipoInformeModerador);
        tvTipoInformeModerador = (TextView) view.findViewById(R.id.tvTipoInformeModerador);

        String [] tipoInforme = {"Ambiental","Limpieza","Denuncia"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_generico, tipoInforme);

        //Seteo valores iniciales
       // spTipoInforme.setAdapter(adapter);
        etFechaInicio.setText(getFecha(dpFechaInicio).toString());
        tvTipoInformeModerador.setText("Tipo Informe");
        btnFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dpFechaInicio.getVisibility() == View.VISIBLE) {
                    // Si el DatePicker ya está visible, ocúltalo al hacer clic en el botón de selección
                    dpFechaInicio.setVisibility(View.GONE);
                } else {
                    // Si el DatePicker está oculto, muéstralo al hacer clic en el botón de selección
                    dpFechaInicio.setVisibility(View.VISIBLE);
                }
            }
        });
        btnFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dpFechaFin.getVisibility() == View.VISIBLE) {
                    // Si el DatePicker ya está visible, ocúltalo al hacer clic en el botón de selección
                    dpFechaFin.setVisibility(View.GONE);
                } else {
                    // Si el DatePicker está oculto, muéstralo al hacer clic en el botón de selección
                    dpFechaFin.setVisibility(View.VISIBLE);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dpFechaInicio.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                    etFechaInicio.setText(getFecha(dpFechaInicio));
                }
            });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dpFechaFin.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                    etFechaFin.setText(getFecha(dpFechaFin));
                }
            });
        }


        return inflater.inflate(R.layout.fragment_crear_informe_moderador, container, false);
    }

    public String getFecha(DatePicker dp){
        String dia = String.valueOf(dp.getDayOfMonth());
        String mes = String.valueOf(dp.getDayOfMonth());
        String anio = String.valueOf(dp.getDayOfMonth());

        String fecha = dia + "/" + mes + "/" + anio;

        return fecha;
    }




}