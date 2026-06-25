package com.example.trackify;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class AjustesActivity extends AppCompatActivity {

    private Spinner spIdioma;
    private Spinner spTema;
    private Spinner spOrdenListado;
    private Spinner spValoracionMaxima;

    private Button btnGuardarAjustes;
    private Button btnRestablecerAjustes;
    private Button btnVolver;

    private SharedPreferences preferences;

    public static final String PREFS_NAME = "trackify_ajustes";

    public static final String KEY_IDIOMA = "idioma";
    public static final String KEY_TEMA = "tema";
    public static final String KEY_ORDEN_LISTADO = "orden_listado";
    public static final String KEY_VALORACION_MAXIMA = "valoracion_maxima";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        spIdioma = findViewById(R.id.spIdioma);
        spTema = findViewById(R.id.spTema);
        spOrdenListado = findViewById(R.id.spOrdenListado);
        spValoracionMaxima = findViewById(R.id.spValoracionMaxima);

        btnGuardarAjustes = findViewById(R.id.btnGuardarAjustes);
        btnRestablecerAjustes = findViewById(R.id.btnRestablecerAjustes);
        btnVolver = findViewById(R.id.btnVolver);

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        configurarSpinners();
        cargarPreferencias();

        btnGuardarAjustes.setOnClickListener(v -> guardarPreferencias());
        btnRestablecerAjustes.setOnClickListener(v -> restablecerPreferencias());
        btnVolver.setOnClickListener(v -> finish());
    }

    private void configurarSpinners() {
        configurarSpinner(spIdioma, new String[]{"Español", "English"});
        configurarSpinner(spTema, new String[]{"Claro", "Oscuro"});
        configurarSpinner(spOrdenListado, new String[]{"Título", "Valoración", "Estado"});
        configurarSpinner(spValoracionMaxima, new String[]{"5", "10"});
    }

    private void configurarSpinner(Spinner spinner, String[] opciones) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                opciones
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void cargarPreferencias() {
        seleccionarValor(spIdioma, preferences.getString(KEY_IDIOMA, "Español"));
        seleccionarValor(spTema, preferences.getString(KEY_TEMA, "Claro"));
        seleccionarValor(spOrdenListado, preferences.getString(KEY_ORDEN_LISTADO, "Título"));
        seleccionarValor(spValoracionMaxima, preferences.getString(KEY_VALORACION_MAXIMA, "5"));
    }

    private void seleccionarValor(Spinner spinner, String valor) {
        for (int i = 0; i < spinner.getCount(); i++) {
            String item = spinner.getItemAtPosition(i).toString();

            if (item.equalsIgnoreCase(valor)) {
                spinner.setSelection(i);
                return;
            }
        }
    }

    private void guardarPreferencias() {
        preferences.edit()
                .putString(KEY_IDIOMA, spIdioma.getSelectedItem().toString())
                .putString(KEY_TEMA, spTema.getSelectedItem().toString())
                .putString(KEY_ORDEN_LISTADO, spOrdenListado.getSelectedItem().toString())
                .putString(KEY_VALORACION_MAXIMA, spValoracionMaxima.getSelectedItem().toString())
                .apply();

        ToastTrackify.mostrar(
                AjustesActivity.this,
                "Ajustes guardados correctamente"
        );
    }

    private void restablecerPreferencias() {
        preferences.edit()
                .putString(KEY_IDIOMA, "Español")
                .putString(KEY_TEMA, "Claro")
                .putString(KEY_ORDEN_LISTADO, "Título")
                .putString(KEY_VALORACION_MAXIMA, "5")
                .apply();

        cargarPreferencias();

        ToastTrackify.mostrar(
                AjustesActivity.this,
                "Ajustes restablecidos"
        );
    }
}