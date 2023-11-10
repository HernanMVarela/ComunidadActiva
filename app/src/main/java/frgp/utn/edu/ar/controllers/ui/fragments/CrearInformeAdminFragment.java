package frgp.utn.edu.ar.controllers.ui.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
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
import frgp.utn.edu.ar.controllers.databinding.FragmentCrearInformeAdminBinding;
import frgp.utn.edu.ar.controllers.utils.LogService;
import frgp.utn.edu.ar.controllers.utils.LogsEnum;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;


public class CrearInformeAdminFragment extends Fragment  implements View.OnFocusChangeListener, View.OnClickListener,  DatePickerDialog.OnDateSetListener {

    private FragmentCrearInformeAdminBinding binding;
    private EditText fechaDesde, fechaHasta;
    private Date dateDesde, dateHasta;
    private Calendar mCalendar;
    private SimpleDateFormat mFormat;
    private Usuario usuario = new Usuario();
    private InformesAdminRepository informesAdminRepository = new InformesAdminRepository();
    private LogService logger = new LogService();
    private SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private JSONArray pdfData = new JSONArray();

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
        usuario = sharedPreferences.getUsuarioData(getContext());
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnUsuariosActivosPorRol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    pdfData = informesAdminRepository.listarUsuariosActivosPorRol(dateDesde, dateHasta);
                    try {
                        System.out.println(pdfData);
                        crearInforme(pdfData, "usuariosActivosPorRol");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        binding.btnUsuariosNuevosRegistrados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    pdfData = informesAdminRepository.listarUsuariosNuevosRegistrados(dateDesde, dateHasta);
                    try {
                        crearInforme(pdfData, "usuariosNuevosRegistrados");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        binding.btnUsuariosPorEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    pdfData = informesAdminRepository.listarUsuariosPorEstado(dateDesde, dateHasta);
                    try {
                        crearInforme(pdfData, "usuariosPorEstado");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        binding.btnReportesPorCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    pdfData = informesAdminRepository.listarReportesPorCategoria(dateDesde, dateHasta);
                    try {
                        crearInforme(pdfData, "reportesPorCategoria");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        binding.btnProyectosPorCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Funcionalidad a la espera de fix BD y codigo", Toast.LENGTH_LONG).show();
                /*
                if(isFormValid()) {
                    pdfData = informesAdminRepository.listarProyectosPorCategoria(dateDesde, dateHasta);
                    try {
                        crearInformeUsuariosActivosPorRol(pdfData, "proyectosPorCategoria");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                 */
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

        if (fechaDesde.hasFocus())
            fechaDesde.setText(mFormat.format(mCalendar.getTime()));
        if (fechaHasta.hasFocus())
            fechaHasta.setText(mFormat.format(mCalendar.getTime()));
    }

    private boolean isFormValid() {
        //CHECK FORM VACIO
        if (fechaDesde.getText().toString().isEmpty() ||
                fechaHasta.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Debe completar todos los campos", Toast.LENGTH_LONG).show();
            return false;
        }

        //CHECK FECHA NO FUTURA
        if (mCalendar.getTime().after(Calendar.getInstance().getTime())) {
            Toast.makeText(getContext(), "Las fechas no puede mayores a la fecha de hoy", Toast.LENGTH_LONG).show();
            return false;
        }
        //CHECK FECHA DESDE NO PUEDE SER MAYOR A HASTA
        if (fechaDesde.getText().toString().compareTo(fechaHasta.getText().toString()) > 0) {
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

    private void crearInforme(JSONArray pdfData, String reporte)  throws IOException {
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
            switch(reporte) {
                case("usuariosActivosPorRol"):
                    canvas.drawText("USUARIOS ACTIVOS POR ROL", pageInfo.getPageWidth() / 2, 80, paint);
                    break;
                case("usuariosNuevosRegistrados"):
                    canvas.drawText("USUARIOS NUEVOS REGISTRADOS", pageInfo.getPageWidth() / 2, 80, paint);
                    break;
                case("usuariosPorEstado"):
                    canvas.drawText("USUARIOS POR ESTADO", pageInfo.getPageWidth() / 2, 80, paint);
                    break;
                case("reportesPorCategoria"):
                    canvas.drawText("REPORTES POR CATEGORIA", pageInfo.getPageWidth() / 2, 80, paint);
                    break;
                case("proyectosPorCategoria"):
                    canvas.drawText("PROYECTOS POR CATEGORIA", pageInfo.getPageWidth() / 2, 80, paint);
                    break;
            }
            //WRITE INFO AS SUBTITLE
            paint.setTextSize(8);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("Desde " + fechaDesde.getText() + " hasta " + fechaHasta.getText(), pageInfo.getPageWidth() / 2, 100, paint);
            canvas.drawText("Fecha Creacion: " + Calendar.getInstance().getTime(), pageInfo.getPageWidth() / 2, 110, paint);
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
                    switch(reporte) {
                        case("usuariosActivosPorRol"):
                            canvas.drawText("ROL: " + pdfData.getJSONObject(i).getString("rol") + "  ---  " + "CANTIDAD: " + pdfData.getJSONObject(i).getString("cantidad"), 50, y, paintText);
                            break;
                        case("usuariosNuevosRegistrados"):
                            canvas.drawText("FECHA: " + pdfData.getJSONObject(i).getString("fecha_registro") + "  ---  " + "CANTIDAD: " + pdfData.getJSONObject(i).getString("cantidad"), 50, y, paintText);
                            break;
                        case("usuariosPorEstado"):
                            canvas.drawText("ESTADO: " + pdfData.getJSONObject(i).getString("estado") + "  ---  " + "CANTIDAD: " + pdfData.getJSONObject(i).getString("cantidad"), 50, y, paintText);
                            break;
                        case("reportesPorCategoria"):
                            canvas.drawText("CATEGORIA: " + pdfData.getJSONObject(i).getString("tipo_reporte") + "  ---  " + "CANTIDAD: " + pdfData.getJSONObject(i).getString("cantidad"), 50, y, paintText);
                            break;
                        case("proyectosPorCategoria"):
                            canvas.drawText("CATEGORIA: " + pdfData.getJSONObject(i).getString("tipo_proyecto") + "  ---  " + "CANTIDAD: " + pdfData.getJSONObject(i).getString("cantidad"), 50, y, paintText);
                            break;
                    }
                } catch (JSONException e) {
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
            switch(reporte) {
                case("usuariosActivosPorRol"):
                    fileName = "Informe Usuarios Activos Por Rol.pdf";
                    break;
                case("usuariosNuevosRegistrados"):
                    fileName = "Informe Usuarios Nuevos Registrados.pdf";
                    break;
                case("usuariosPorEstado"):
                    fileName = "Informe Usuarios Por Estado.pdf";
                    break;
                case("reportesPorCategoria"):
                    fileName = "Informe Reportes Por Categoria.pdf";
                    break;
                case("proyectosPorCategoria"):
                    fileName = "Informe Proyectos Por Categoria.pdf";
                    break;
            }
            File file = new File(downloadsDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                pdfDocument.writeTo(fos);
                pdfDocument.close();
                fos.close();
                logger.log(usuario.getId(), LogsEnum.CREACION_INFORME, String.format("Se creo el %s", fileName));
                Toast.makeText(getContext(), "Se ha creado el reporte.", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error al crear el reporte.", Toast.LENGTH_LONG).show();
                throw new RuntimeException(e);
            }
        }
    }
}




