package com.example.trackify;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import API.API;
import API.ContenidoModel;
import API.TokenManager;
import API.UtilJSONParser;
import API.UtilREST;

import org.json.JSONObject;

public class FormularioActivity extends AppCompatActivity {

    private EditText etTitulo;
    private Spinner spGenero;
    private Spinner spEstado;
    private EditText etValoracion;
    private EditText etDescripcion;
    private EditText etImagenUrl;
    private Button btnGuardar;

    private String modo;
    private long contenidoId;
    private String tipoSeleccionado;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        etTitulo = findViewById(R.id.etTitulo);
        spGenero = findViewById(R.id.spGenero);
        spEstado = findViewById(R.id.spEstado);
        etValoracion = findViewById(R.id.etValoracion);
        etDescripcion = findViewById(R.id.etDescripcion);
        etImagenUrl = findViewById(R.id.etImagenUrl);
        btnGuardar = findViewById(R.id.btnGuardar);

        token = TokenManager.getToken(this);

        configurarSpinners();

        modo = getIntent().getStringExtra("modo");

        if (modo == null) {
            modo = "crear";
        }

        if (modo.equals("editar")) {
            contenidoId = getIntent().getLongExtra("contenidoId", -1);

            if (contenidoId == -1) {
                ToastTrackify.mostrar(this, getString(R.string.contenido_no_valido));
                finish();
                return;
            }

            btnGuardar.setText(getString(R.string.actualizar_contenido_mayus));
            cargarContenido();

        } else {
            tipoSeleccionado = getIntent().getStringExtra("tipo");

            if (tipoSeleccionado == null || tipoSeleccionado.isEmpty()) {
                tipoSeleccionado = "Pelicula";
            }

            btnGuardar.setText(getString(R.string.guardar_contenido_mayus));
        }

        btnGuardar.setOnClickListener(v -> guardar());
    }

    private void configurarSpinners() {
        ArrayAdapter<CharSequence> adapterGeneros =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.generos_array,
                        android.R.layout.simple_spinner_item
                );

        adapterGeneros.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spGenero.setAdapter(adapterGeneros);

        ArrayAdapter<CharSequence> adapterEstados =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.estados_array,
                        android.R.layout.simple_spinner_item
                );

        adapterEstados.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spEstado.setAdapter(adapterEstados);
    }

    private void cargarContenido() {
        API.obtenerContenido(contenidoId, token, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                ContenidoModel contenido = UtilJSONParser.parseContenido(r.content);

                if (contenido == null) {
                    ToastTrackify.mostrar(FormularioActivity.this, getString(R.string.error_leyendo_contenido));
                    finish();
                    return;
                }

                etTitulo.setText(contenido.getTitulo());
                etDescripcion.setText(contenido.getDescripcion());
                etImagenUrl.setText(contenido.getImagenUrl());

                seleccionarSpinner(spGenero, contenido.getGenero());
                seleccionarSpinner(spEstado, contenido.getEstado());

                if (contenido.getValoracion() != null) {
                    etValoracion.setText(String.valueOf(contenido.getValoracion()));
                }

                tipoSeleccionado = contenido.getTipo();
            }

            @Override
            public void onError(UtilREST.Response r) {
                ToastTrackify.mostrar(FormularioActivity.this, getString(R.string.error_cargando_contenido));
                finish();
            }
        });
    }

    private void seleccionarSpinner(Spinner spinner, String valor) {
        if (valor == null) {
            return;
        }

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(valor)) {
                spinner.setSelection(i);
                return;
            }
        }
    }

    private void guardar() {
        if (!validarFormulario()) {
            return;
        }

        JSONObject json = crearJsonDesdeFormulario();

        if (modo.equals("editar")) {
            actualizarContenido(json);
        } else {
            crearContenido(json);
        }
    }

    private boolean validarFormulario() {
        String titulo = etTitulo.getText().toString().trim();
        String valoracionTexto = etValoracion.getText().toString().trim();

        if (titulo.isEmpty()) {
            etTitulo.setError(getString(R.string.introduce_titulo));
            return false;
        }

        if (valoracionTexto.isEmpty()) {
            etValoracion.setError(getString(R.string.introduce_valoracion));
            return false;
        }

        int valoracion;

        try {
            valoracion = Integer.parseInt(valoracionTexto);
        } catch (NumberFormatException ex) {
            etValoracion.setError(getString(R.string.valoracion_debe_numero));
            return false;
        }

        if (valoracion < 1 || valoracion > 5) {
            etValoracion.setError(getString(R.string.valoracion_entre_1_5));
            return false;
        }

        return true;
    }

    private JSONObject crearJsonDesdeFormulario() {
        String titulo = etTitulo.getText().toString().trim();
        String genero = spGenero.getSelectedItem().toString();
        String estado = spEstado.getSelectedItem().toString();
        String descripcion = etDescripcion.getText().toString().trim();
        String imagenUrl = etImagenUrl.getText().toString().trim();
        int valoracion = Integer.parseInt(etValoracion.getText().toString().trim());

        return UtilJSONParser.createContenido(
                titulo,
                descripcion,
                genero,
                tipoSeleccionado,
                estado,
                valoracion,
                imagenUrl
        );
    }

    private void crearContenido(JSONObject json) {
        API.crearContenido(json, token, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                ToastTrackify.mostrar(FormularioActivity.this, getString(R.string.contenido_creado));
                finish();
            }

            @Override
            public void onError(UtilREST.Response r) {
                ToastTrackify.mostrar(FormularioActivity.this, getString(R.string.error_creando_contenido));
            }
        });
    }

    private void actualizarContenido(JSONObject json) {
        API.actualizarContenido(contenidoId, json, token, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                ToastTrackify.mostrar(FormularioActivity.this, getString(R.string.contenido_actualizado));
                finish();
            }

            @Override
            public void onError(UtilREST.Response r) {
                ToastTrackify.mostrar(FormularioActivity.this, getString(R.string.error_actualizando_contenido));
            }
        });
    }
}