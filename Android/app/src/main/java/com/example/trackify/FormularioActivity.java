package com.example.trackify;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import API.API;
import API.ContenidoModel;
import API.TokenManager;
import API.UtilJSONParser;
import API.UtilREST;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

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
                Toast.makeText(this, "Contenido no válido", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            btnGuardar.setText("ACTUALIZAR CONTENIDO");
            cargarContenido();

        } else {
            tipoSeleccionado = getIntent().getStringExtra("tipo");

            if (tipoSeleccionado == null || tipoSeleccionado.isEmpty()) {
                tipoSeleccionado = "Pelicula";
            }

            btnGuardar.setText("GUARDAR CONTENIDO");
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
                    Toast.makeText(FormularioActivity.this, "Error leyendo contenido", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(FormularioActivity.this, "Error cargando contenido", Toast.LENGTH_SHORT).show();
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
            etTitulo.setError("Introduce un título");
            return false;
        }

        if (valoracionTexto.isEmpty()) {
            etValoracion.setError("Introduce una valoración");
            return false;
        }

        int valoracion;

        try {
            valoracion = Integer.parseInt(valoracionTexto);
        } catch (NumberFormatException ex) {
            etValoracion.setError("La valoración debe ser un número");
            return false;
        }

        if (valoracion < 1 || valoracion > 10) {
            etValoracion.setError("La valoración debe estar entre 1 y 10");
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
                Toast.makeText(FormularioActivity.this, "Contenido creado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(UtilREST.Response r) {
                Toast.makeText(FormularioActivity.this, "Error creando contenido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarContenido(JSONObject json) {
        API.actualizarContenido(contenidoId, json, token, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                Toast.makeText(FormularioActivity.this, "Contenido actualizado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(UtilREST.Response r) {
                Toast.makeText(FormularioActivity.this, "Error actualizando contenido", Toast.LENGTH_SHORT).show();
            }
        });
    }
}