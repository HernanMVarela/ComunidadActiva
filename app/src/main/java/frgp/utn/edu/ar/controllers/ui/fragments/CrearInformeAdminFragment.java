package frgp.utn.edu.ar.controllers.ui.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.repository.informesAdmin.InformesAdminRepository;
import frgp.utn.edu.ar.controllers.databinding.FragmentCrearInformeAdminBinding;


public class CrearInformeAdminFragment extends Fragment  implements View.OnFocusChangeListener, View.OnClickListener,  DatePickerDialog.OnDateSetListener {

    private FragmentCrearInformeAdminBinding binding;
    private EditText fechaDesde, fechaHasta;
    private Date dateDesde, dateHasta;
    private Calendar mCalendar;
    private SimpleDateFormat mFormat;
    InformesAdminRepository informesAdminRepository = new InformesAdminRepository();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCrearInformeAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fechaDesde = root.findViewById(R.id.etFechaInicioInformeAdmin);
        fechaHasta = root.findViewById(R.id.etFechaFinInformeAdmin);
        fechaDesde.setOnFocusChangeListener(this);
        fechaHasta.setOnFocusChangeListener(this);
        fechaDesde.setOnClickListener(this);
        fechaHasta.setOnClickListener(this);
        fechaDesde.setShowSoftInputOnFocus(false);
        fechaHasta.setShowSoftInputOnFocus(false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnUsuariosActivosPorRol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuariosActivosPorRol(v);
            }
        });

        binding.btnUsuariosNuevosRegistrados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuariosNuevosRegistrados(v);
            }
        });

        binding.btnUsuariosPorEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuariosPorEstado(v);
            }
        });

        binding.btnReportesPorCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportesPorCategoria(v);
            }
        });

        binding.btnProyectosPorCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proyectosPorCategoria(v);
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            showPicker(v);
        }
    }

    @Override
    public void onClick(View view) {
        showPicker(view);
    }

    private void showPicker(View view) {
        if (mCalendar == null)
            mCalendar = Calendar.getInstance();

        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);

        new DatePickerDialog(view.getContext(), this, year, month, day).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if(fechaDesde.hasFocus())
            fechaDesde.setText(mFormat.format(mCalendar.getTime()));
        if(fechaHasta.hasFocus())
            fechaHasta.setText(mFormat.format(mCalendar.getTime()));
    }

    public boolean isFormValid() {
        //CHECK FORM VACIO
        if (fechaDesde.getText().toString().isEmpty() ||
            fechaHasta.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Debe completar todos los campos", Toast.LENGTH_LONG).show();
            return false;
        }

        //CHECK FECHA NO FUTURA
        if(mCalendar.getTime().after(Calendar.getInstance().getTime())){
            Toast.makeText(getContext(), "Las fechas no puede mayores a la fecha de hoy", Toast.LENGTH_LONG).show();
            return false;
        }
        //CHECK FECHA DESDE NO PUEDE SER MAYOR A HASTA
        if(fechaDesde.getText().toString().compareTo(fechaHasta.getText().toString()) > 0){
            Toast.makeText(getContext(), "La fecha de inicio no puede ser mayor a la fecha de fin", Toast.LENGTH_LONG).show();
            return false;
        }
        //CHECH FECHA VALID
        try {
            String date = fechaDesde.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            dateDesde = sdf.parse(date);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ingrese una fecha de inicio valida", Toast.LENGTH_LONG).show();
            return false;
        }
        //CHECH FECHA VALID
        try {
            String date = fechaHasta.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            dateHasta = sdf.parse(date);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ingrese una fecha de fin valida", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public void usuariosActivosPorRol(View view) {
        if(isFormValid())
            System.out.println(informesAdminRepository.listarUsuariosActivosPorRol(dateDesde, dateHasta));

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