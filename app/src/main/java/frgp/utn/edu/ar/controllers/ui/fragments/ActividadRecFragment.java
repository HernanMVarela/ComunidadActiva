package frgp.utn.edu.ar.controllers.ui.fragments;

import android.Manifest;
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
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Logs;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.repository.log.LogRepository;
import frgp.utn.edu.ar.controllers.databinding.FragmentActividadRecienteBinding;
import frgp.utn.edu.ar.controllers.ui.adapters.ListaActividadRecienteAdapter;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class ActividadRecFragment extends Fragment {

    private FragmentActividadRecienteBinding binding;
    LogRepository logRepository = new LogRepository();
    SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private List<Logs> listaLogs;
    private Usuario usuario;
    private ListView listaActividadReciente;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentActividadRecienteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        usuario = sharedPreferences.getUsuarioData(this.getContext());
        listaLogs = logRepository.listarLogsPorId(usuario.getId());
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListaActividadRecienteAdapter adapter = new ListaActividadRecienteAdapter(this.getContext(), listaLogs);
        listaActividadReciente = view.findViewById(R.id.listActividadReciente);
        listaActividadReciente.setAdapter(adapter);

        binding.btnInformeActividadReciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createPdfReport();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void createPdfReport() throws IOException {
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
            canvas.drawText("Reporte de actividad reciente", pageInfo.getPageWidth() / 2, 80, paint);
            //WRITE INFO AS SUBTITLE
            paint.setTextSize(8);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("Usuario: " + usuario.getNombre() + " " + usuario.getApellido(), pageInfo.getPageWidth() / 2, 100, paint);
            canvas.drawText("Fecha: " + new Date(), pageInfo.getPageWidth() / 2, 110, paint);
            //WRITE LOGS
            paint.setTextSize(6);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            int y = 150;
            paint.setStrokeWidth(1);
            canvas.drawLine(20, y, pageInfo.getPageWidth() - 100, y, paint);
            y += 10;
            for (Logs log : listaLogs) {
                canvas.drawText(log.getFecha() + " | " + log.getDescripcion(), 100, y, paint);
                y += 10;
                paint.setStrokeWidth(1);
                canvas.drawLine(20, y, pageInfo.getPageWidth() - 100, y, paint);
                y += 10;
            }
            pdfDocument.finishPage(page);

            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String fileName = "ReporteActividadReciente.pdf";
            File file = new File(downloadsDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                pdfDocument.writeTo(fos);
                pdfDocument.close();
                fos.close();
                Toast.makeText(getContext(), "Se ha creado el informe de actividad reciente", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error al crear el informe de actividad reciente", Toast.LENGTH_LONG).show();
                throw new RuntimeException(e);
            }
        }


    }
}