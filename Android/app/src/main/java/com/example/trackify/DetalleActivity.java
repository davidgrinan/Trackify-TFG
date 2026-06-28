package com.example.trackify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import API.API;
import API.ContenidoModel;
import API.TokenManager;
import API.UtilJSONParser;
import API.UtilREST;

public class DetalleActivity extends AppCompatActivity {

    private ImageView imgPortadaDetalle;
    private TextView tvTituloDetalle;
    private TextView tvTipoDetalle;
    private TextView tvGeneroDetalle;
    private TextView tvValoracionDetalle;
    private TextView tvEstadoDetalle;
    private TextView tvDescripcionDetalle;

    private Button btnEditar;
    private Button btnEliminar;
    private Button btnVolver;

    private long contenidoId;
    private String token;
    private ContenidoModel contenidoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        imgPortadaDetalle = findViewById(R.id.imgPortadaDetalle);
        tvTituloDetalle = findViewById(R.id.tvTituloDetalle);
        tvTipoDetalle = findViewById(R.id.tvTipoDetalle);
        tvGeneroDetalle = findViewById(R.id.tvGeneroDetalle);
        tvValoracionDetalle = findViewById(R.id.tvValoracionDetalle);
        tvEstadoDetalle = findViewById(R.id.tvEstadoDetalle);
        tvDescripcionDetalle = findViewById(R.id.tvDescripcionDetalle);

        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolver = findViewById(R.id.btnVolverDetalle);

        token = TokenManager.getToken(this);
        contenidoId = getIntent().getLongExtra("contenidoId", -1);

        if (contenidoId == -1) {
            ToastTrackify.mostrar(this, getString(R.string.contenido_no_valido));
            finish();
            return;
        }

        btnEditar.setOnClickListener(v -> abrirEditar());
        btnEliminar.setOnClickListener(v -> confirmarEliminar());
        btnVolver.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDetalle();
    }

    private void cargarDetalle() {
        API.obtenerContenido(contenidoId, token, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                contenidoActual = UtilJSONParser.parseContenido(r.content);

                if (contenidoActual == null) {
                    ToastTrackify.mostrar(
                            DetalleActivity.this,
                            getString(R.string.error_leyendo_contenido)
                    );
                    finish();
                    return;
                }

                pintarDatos();
            }

            @Override
            public void onError(UtilREST.Response r) {
                ToastTrackify.mostrar(
                        DetalleActivity.this,
                        getString(R.string.error_cargando_contenido)
                );

                if (r.responseCode == 401) {
                    TokenManager.clearToken(DetalleActivity.this);
                    startActivity(new Intent(DetalleActivity.this, LoginActivity.class));
                }

                finish();
            }
        });
    }

    private void pintarDatos() {
        tvTituloDetalle.setText(contenidoActual.getTitulo());
        tvTipoDetalle.setText(getString(R.string.tipo_formato, contenidoActual.getTipo()));
        tvGeneroDetalle.setText(getString(R.string.genero_formato, contenidoActual.getGenero()));
        tvEstadoDetalle.setText(getString(R.string.estado_formato, contenidoActual.getEstado()));
        tvDescripcionDetalle.setText(contenidoActual.getDescripcion());

        pintarValoracion();

        if (contenidoActual.getImagenUrl() != null && !contenidoActual.getImagenUrl().isEmpty()) {
            Glide.with(this)
                    .load(contenidoActual.getImagenUrl())
                    .placeholder(R.drawable.logo_trackify)
                    .error(R.drawable.logo_trackify)
                    .into(imgPortadaDetalle);
        } else {
            imgPortadaDetalle.setImageResource(R.drawable.logo_trackify);
        }
    }

    private void pintarValoracion() {
        if (contenidoActual.getValoracion() == null) {
            tvValoracionDetalle.setText(getString(R.string.sin_valoracion));
            return;
        }

        SharedPreferences preferences =
                getSharedPreferences(AjustesActivity.PREFS_NAME, MODE_PRIVATE);

        String valoracionMaxima =
                preferences.getString(
                        AjustesActivity.KEY_VALORACION_MAXIMA,
                        AjustesActivity.VALORACION_5
                );

        int valoracion = contenidoActual.getValoracion();

        if (valoracionMaxima.equals(AjustesActivity.VALORACION_10)) {
            int valoracionSobre10;

            if (valoracion <= 5) {
                valoracionSobre10 = valoracion * 2;
            } else {
                valoracionSobre10 = valoracion;
            }

            String textoValoracion =
                    getString(R.string.valoracion_formato, String.valueOf(valoracionSobre10));

            textoValoracion = textoValoracion.replace("/5", "/10");

            tvValoracionDetalle.setText(textoValoracion);

        } else {
            tvValoracionDetalle.setText(
                    getString(R.string.valoracion_formato, String.valueOf(valoracion))
            );
        }
    }

    private void abrirEditar() {
        Intent intent = new Intent(DetalleActivity.this, FormularioActivity.class);
        intent.putExtra("modo", "editar");
        intent.putExtra("contenidoId", contenidoId);
        startActivity(intent);
    }

    private void confirmarEliminar() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.eliminar_contenido))
                .setMessage(getString(R.string.confirmar_eliminar_contenido))
                .setPositiveButton(getString(R.string.eliminar), (dialog, which) -> eliminarContenido())
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();
    }

    private void eliminarContenido() {
        API.eliminarContenido(contenidoId, token, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                ToastTrackify.mostrar(
                        DetalleActivity.this,
                        getString(R.string.contenido_eliminado)
                );
                finish();
            }

            @Override
            public void onError(UtilREST.Response r) {
                ToastTrackify.mostrar(
                        DetalleActivity.this,
                        getString(R.string.error_eliminando_contenido)
                );
            }
        });
    }
}