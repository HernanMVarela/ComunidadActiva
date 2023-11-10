package frgp.utn.edu.ar.controllers.ui.fragments;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import frgp.utn.edu.ar.controllers.R;
import frgp.utn.edu.ar.controllers.data.model.Logs;
import frgp.utn.edu.ar.controllers.data.model.Usuario;
import frgp.utn.edu.ar.controllers.data.remote.usuario.DMABuscarUsuarioPorID;
import frgp.utn.edu.ar.controllers.data.repository.log.LogRepository;
import frgp.utn.edu.ar.controllers.databinding.FragmentActividadRecienteBinding;
import frgp.utn.edu.ar.controllers.databinding.FragmentHistorialModeracionBinding;
import frgp.utn.edu.ar.controllers.ui.adapters.ListaActividadRecienteAdapter;
import frgp.utn.edu.ar.controllers.ui.adapters.ListaHistorialDeModeracionAdapter;
import frgp.utn.edu.ar.controllers.ui.dialogs.UserDetailDialogFragment;
import frgp.utn.edu.ar.controllers.ui.viewmodels.HistorialModeracionViewModel;
import frgp.utn.edu.ar.controllers.utils.SharedPreferencesService;

public class HistorialModeracionFragment extends Fragment {

    private FragmentHistorialModeracionBinding binding;

    private LogRepository logRepository = new LogRepository();
    private SharedPreferencesService sharedPreferences = new SharedPreferencesService();
    private List<Logs> listaLogsModeracion;
    private Usuario loggedUser ;
    private ListView listaActividadRecienteModeracion;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistorialModeracionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        loggedUser  = sharedPreferences.getUsuarioData(this.getContext());
        if(loggedUser.getTipo().getTipo().equals("ADMINISTRADOR")){
            listaLogsModeracion = logRepository.listarLogsModeracion();
        }else{
            listaLogsModeracion = logRepository.listarLogsModeracionPorId(loggedUser.getId());
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListaHistorialDeModeracionAdapter adapter = new ListaHistorialDeModeracionAdapter(this.getContext(), listaLogsModeracion);
        listaActividadRecienteModeracion = view.findViewById(R.id.listModeracionReciente);
        listaActividadRecienteModeracion.setAdapter(adapter);

        binding.btnInformeModeracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createPdfReport();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        if(loggedUser.getTipo().getTipo().equals("ADMINISTRADOR")){
            comportamiento_click_listado();
        }
    }

    private void comportamiento_click_listado(){
        listaActividadRecienteModeracion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logs log = (Logs)listaActividadRecienteModeracion.getItemAtPosition(position);
                try {
                    DMABuscarUsuarioPorID DMAUserID = new DMABuscarUsuarioPorID(log.getIdUser());
                    DMAUserID.execute();
                    Usuario mod = DMAUserID.get();
                    if(mod != null){
                        UserDetailDialogFragment dialogFragment = UserDetailDialogFragment.newInstance(mod);
                        dialogFragment.show(getFragmentManager(), "user_detail");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void createPdfReport() throws IOException {
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
            canvas.drawText("Usuario: " + loggedUser.getNombre() + " " + loggedUser.getApellido(), pageInfo.getPageWidth() / 2, 100, paint);
            canvas.drawText("Fecha: " + Calendar.getInstance().getTime(), pageInfo.getPageWidth() / 2, 110, paint);
            //WRITE LOGS
            paint.setTextSize(6);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            int y = 150;
            paint.setStrokeWidth(1);
            canvas.drawLine(20, y, pageInfo.getPageWidth() - 100, y, paint);
            y += 10;
            for (Logs log : listaLogsModeracion) {

                String timeStamp = new Timestamp(log.getFecha().getTime()).toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = sdf.parse(timeStamp);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                sdf = new SimpleDateFormat("d/MM/yyyy h:m a");

                canvas.drawText(sdf.format(date) + " | " + log.getDescripcion(), 100, y, paint);
                y += 10;
                paint.setStrokeWidth(1);
                canvas.drawLine(20, y, pageInfo.getPageWidth() - 100, y, paint);
                y += 10;
            }
            pdfDocument.finishPage(page);

            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String fileName = "ReporteModeracionReciente.pdf";
            File file = new File(downloadsDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                pdfDocument.writeTo(fos);
                pdfDocument.close();
                fos.close();
                Toast.makeText(getContext(), "Se ha creado el informe de moderacion", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error al crear el informe de moderacion", Toast.LENGTH_LONG).show();
                throw new RuntimeException(e);
            }
        }
    }

}