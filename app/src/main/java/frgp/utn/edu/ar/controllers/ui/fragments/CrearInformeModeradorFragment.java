package frgp.utn.edu.ar.controllers.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.repository.informesAdmin.InformesAdminRepository;
import frgp.utn.edu.ar.controllers.data.repository.informesModerador.InformesModeradorRepository;
import frgp.utn.edu.ar.controllers.databinding.FragmentCrearInformeAdminBinding;
import frgp.utn.edu.ar.controllers.databinding.FragmentCrearInformeModeradorBinding;
import frgp.utn.edu.ar.controllers.ui.viewmodels.CrearInformeViewModel;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;


public class CrearInformeModeradorFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener,  DatePickerDialog.OnDateSetListener  {


    private CrearInformeViewModel mViewModel;
    private FragmentCrearInformeModeradorBinding binding;
    private EditText etFechaInicio, etFechaFin;
    private Date dateDesde, dateHasta;
    private SimpleDateFormat mFormat;
    private Calendar mCalendar;
    private Usuario usuario = new Usuario();
    private LogService logger = new LogService();
    private SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private JSONArray pdfData = new JSONArray();
    private InformesModeradorRepository informesModeradorRepository = new InformesModeradorRepository();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCrearInformeModeradorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //vinculo variables a objetos del view
        mFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        etFechaInicio =  root.findViewById(R.id.editTextFechaInicioModerador);
        etFechaFin =  root.findViewById(R.id.editTextFechaFinModerador);
        etFechaInicio.setOnFocusChangeListener(this);
        etFechaFin.setOnFocusChangeListener(this);
        etFechaInicio.setOnClickListener(this);
        etFechaFin.setOnClickListener(this);
        etFechaInicio.setShowSoftInputOnFocus(false);
        etFechaFin.setShowSoftInputOnFocus(false);
        usuario = sharedPreferences.getUsuarioData(getContext());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnCrearInformeDenunciasCerradas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    pdfData = informesModeradorRepository.listarDenunciasCerradas(dateDesde, dateHasta);
                    try {
                        System.out.println(pdfData);
                        crearInforme(pdfData, "denunciasCerradas");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void crearInforme(JSONArray pdfData, String informe)  throws IOException {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Si no tiene permisos, se solicitan al usuario
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            // Si tiene permisos, se crea el PDF
            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            //WRITE TITLE
            Paint paint = new Paint();
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint.setTextSize(12);
            switch(informe) {
                case("denunciasCerradas"):
                    canvas.drawText("DENUNCIAS CERRADAS O CANCELADAS", pageInfo.getPageWidth() / 2, 80, paint);
                    break;

            }
            //WRITE INFO AS SUBTITLE
            paint.setTextSize(8);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("Desde " + etFechaInicio.getText() + " hasta " + etFechaFin.getText(), pageInfo.getPageWidth() / 2, 100, paint);
            canvas.drawText("Fecha Creación: " + Calendar.getInstance().getTime(), pageInfo.getPageWidth() / 2, 110, paint);
            //WRITE INFO
            Paint paintText = new Paint();
            paint.setTextAlign(Paint.Align.LEFT);
            paintText.setTextSize(6);
            paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            int y = 150;
            paintText.setStrokeWidth(1);
            canvas.drawLine(50, y, pageInfo.getPageWidth() - 100, y, paintText);
            y += 10;
            for (int i = 0; i < pdfData.length(); i++) {
                try {
                    switch(informe) {
                        case("denunciasCerradas"):
                            canvas.drawText("TITULO: " + pdfData.getJSONObject(i).getString("TITULO") + "  ---  " + "NOMBRE: " + pdfData.getJSONObject(i).getString("USERNAME") + "  ---  " + "ESTADO: " + pdfData.getJSONObject(i).getString("ESTADO"), 50, y, paintText);
                            break;
                    }
                } catch (JSONException e) {
                    logger.log(usuario.getId(), LogsEnum.CREACION_INFORME, String.format("Error al crear Informe Cantidad denuncias Cerradas", "denunciasCerradas"));
                    e.printStackTrace();
                }
                y += 10;
                paintText.setStrokeWidth(1);
                canvas.drawLine(50, y, pageInfo.getPageWidth() - 100, y, paintText);
                y += 10;
            }
            pdfDocument.finishPage(page);

            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String fileName = "";
            switch(informe) {
                case("denunciasCerradas"):
                    fileName = "Informe Cantidad denuncias Cerradas o Canceladas.pdf";
                    break;
            }
            File file = new File(downloadsDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                pdfDocument.writeTo(fos);
                pdfDocument.close();
                fos.close();
                logger.log(usuario.getId(), LogsEnum.CREACION_INFORME, String.format("Se creo el %s", fileName));
                Toast.makeText(getContext(), "Se ha creado el Informe.", Toast.LENGTH_LONG).show();
            } catch (IOException e) {

                Toast.makeText(getContext(), "Error al crear el Informe.", Toast.LENGTH_LONG).show();
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isFormValid() {
        //CHECK FORM VACIO
        if (etFechaInicio.getText().toString().isEmpty() ||
                etFechaFin.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Debe completar todos los campos", Toast.LENGTH_LONG).show();
            return false;
        }

        //CHECK FECHA NO FUTURA
        if (mCalendar.getTime().after(Calendar.getInstance().getTime())) {
            Toast.makeText(getContext(), "Las fechas no puede mayores a la fecha de hoy", Toast.LENGTH_LONG).show();
            return false;
        }
        //CHECK FECHA DESDE NO PUEDE SER MAYOR A HASTA
        if (etFechaInicio.getText().toString().compareTo(etFechaFin.getText().toString()) > 0) {
            Toast.makeText(getContext(), "La fecha de inicio no puede ser mayor a la fecha de fin", Toast.LENGTH_LONG).show();
            return false;
        }
        //CHECH FECHA VALID
        try {
            String date = etFechaInicio.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            dateDesde = sdf.parse(date);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ingrese una fecha de inicio valida", Toast.LENGTH_LONG).show();
            return false;
        }
        //CHECH FECHA VALID
        try {
            String date = etFechaFin.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            dateHasta = sdf.parse(date);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ingrese una fecha de fin valida", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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

        if (etFechaInicio.hasFocus())
            etFechaInicio.setText(mFormat.format(mCalendar.getTime()));
        if (etFechaFin.hasFocus())
            etFechaFin.setText(mFormat.format(mCalendar.getTime()));
    }
}