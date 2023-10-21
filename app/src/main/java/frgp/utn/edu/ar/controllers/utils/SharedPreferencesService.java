package frgp.utn.edu.ar.controllers.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import frgp.utn.edu.ar.controllers.data.model.Usuario;

public class SharedPreferencesService {
    private final String PREFS_KEY = "comunidad.activa.sharedpreferences";
    public void saveUsuarioData(Context context, Usuario usuario) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
        String json = gson.toJson(usuario);
        editor = settings.edit();
        editor.putString("usuario", json);
        editor.apply();
    }

    public Usuario getUsuarioData(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
        String json = settings.getString("usuario", "");
        return gson.fromJson(json, Usuario.class);
    }

    public void deleteUsuarioData(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.remove("usuario");
        editor.apply();
    }
}
