package frgp.utn.edu.ar.controllers.ui.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.databinding.FragmentCrearInformeAdminBinding;


public class CrearInformeAdminFragment extends Fragment {

    private FragmentCrearInformeAdminBinding binding;
    private EditText fechaDesde, fechaHasta;
    Calendar myCalendar = Calendar.getInstance(); //global

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crear_informe_admin, container, false);

        fechaDesde = (EditText) view.findViewById(R.id.etFechaInicioInformeAdmin);
        fechaHasta = (EditText) view.findViewById(R.id.etFechaFinInformeAdmin);
        return inflater.inflate(R.layout.fragment_crear_informe_admin, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentCrearInformeAdminBinding.bind(view);
        fechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePickerDialog();
            }
        });
    }


    public void showDatePickerDialog(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    };






    public void usuariosActivosPorRol(View view) {

    }

    public void usuariosNuevosRegistrados(View view) {
    }

    public void usuariosPorEstado(View view) {
    }

    public void reportesPorCategoria(View view) {
    }

    public void proyectosPorCategoria(View view) {
    }




}