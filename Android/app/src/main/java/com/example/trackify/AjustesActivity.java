package com.example.trackify;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

public class AjustesActivity extends AppCompatActivity {

    private Spinner spIdioma;
    private Spinner spOrdenListado;
    private Spinner spValoracionMaxima;

    private Button btnGuardarAjustes;
    private Button btnRestablecerAjustes;
    private Button btnVolver;

    private SharedPreferences preferences;

    public static final String PREFS_NAME = "trackify_ajustes";

    public static final String KEY_IDIOMA = "idioma";
    public static final String KEY_ORDEN_LISTADO = "orden_listado";
    public static final String KEY_VALORACION_MAXIMA = "valoracion_maxima";

    public static final String IDIOMA_ES = "es";
    public static final String IDIOMA_EN = "en-rGB";

    public static final String ORDEN_TITULO = "titulo";
    public static final String ORDEN_VALORACION = "valoracion";
    public static final String ORDEN_ESTADO = "estado";

    public static final String VALORACION_5 = "5";
    public static final String VALORACION_10 = "10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        spIdioma = findViewById(R.id.spIdioma);
        spOrdenListado = findViewById(R.id.spOrdenListado);
        spValoracionMaxima = findViewById(R.id.spValoracionMaxima);

        btnGuardarAjustes = findViewById(R.id.btnGuardarAjustes);
        btnRestablecerAjustes = findViewById(R.id.btnRestablecerAjustes);
        btnVolver = findViewById(R.id.btnVolver);

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        cargarPreferencias();

        btnGuardarAjustes.setOnClickListener(v -> guardarPreferencias());
        btnRestablecerAjustes.setOnClickListener(v -> restablecerPreferencias());
        btnVolver.setOnClickListener(v -> finish());
    }

    private void cargarPreferencias() {
        String idiomaGuardado = preferences.getString(KEY_IDIOMA, IDIOMA_ES);
        String ordenGuardado = preferences.getString(KEY_ORDEN_LISTADO, ORDEN_TITULO);
        String valoracionGuardada = preferences.getString(KEY_VALORACION_MAXIMA, VALORACION_5);

        seleccionarIdioma(idiomaGuardado);
        seleccionarOrden(ordenGuardado);
        seleccionarValoracion(valoracionGuardada);
    }

    private void seleccionarIdioma(String idioma) {
        if (idioma.equals(IDIOMA_EN)) {
            spIdioma.setSelection(1);
        } else {
            spIdioma.setSelection(0);
        }
    }

    private void seleccionarOrden(String orden) {
        if (orden.equals(ORDEN_VALORACION)) {
            spOrdenListado.setSelection(1);
        } else if (orden.equals(ORDEN_ESTADO)) {
            spOrdenListado.setSelection(2);
        } else {
            spOrdenListado.setSelection(0);
        }
    }

    private void seleccionarValoracion(String valoracion) {
        if (valoracion.equals(VALORACION_10)) {
            spValoracionMaxima.setSelection(1);
        } else {
            spValoracionMaxima.setSelection(0);
        }
    }

    private void guardarPreferencias() {
        String idiomaSeleccionado = obtenerIdiomaSeleccionado();
        String ordenSeleccionado = obtenerOrdenSeleccionado();
        String valoracionSeleccionada = obtenerValoracionSeleccionada();

        preferences.edit()
                .putString(KEY_IDIOMA, idiomaSeleccionado)
                .putString(KEY_ORDEN_LISTADO, ordenSeleccionado)
                .putString(KEY_VALORACION_MAXIMA, valoracionSeleccionada)
                .apply();

        aplicarIdioma(idiomaSeleccionado);

        ToastTrackify.mostrar(
                AjustesActivity.this,
                getString(R.string.ajustes_guardados)
        );
    }

    private void restablecerPreferencias() {
        preferences.edit()
                .putString(KEY_IDIOMA, IDIOMA_ES)
                .putString(KEY_ORDEN_LISTADO, ORDEN_TITULO)
                .putString(KEY_VALORACION_MAXIMA, VALORACION_5)
                .apply();

        cargarPreferencias();

        aplicarIdioma(IDIOMA_ES);

        ToastTrackify.mostrar(
                AjustesActivity.this,
                getString(R.string.ajustes_restablecidos)
        );
    }

    private String obtenerIdiomaSeleccionado() {
        int posicion = spIdioma.getSelectedItemPosition();

        if (posicion == 1) {
            return IDIOMA_EN;
        }

        return IDIOMA_ES;
    }

    private String obtenerOrdenSeleccionado() {
        int posicion = spOrdenListado.getSelectedItemPosition();

        if (posicion == 1) {
            return ORDEN_VALORACION;
        }

        if (posicion == 2) {
            return ORDEN_ESTADO;
        }

        return ORDEN_TITULO;
    }

    private String obtenerValoracionSeleccionada() {
        int posicion = spValoracionMaxima.getSelectedItemPosition();

        if (posicion == 1) {
            return VALORACION_10;
        }

        return VALORACION_5;
    }

    private void aplicarIdioma(String idioma) {
        AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(idioma)
        );
    }
}