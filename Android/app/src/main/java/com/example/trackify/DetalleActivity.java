package com.example.trackify;

import android.content.Intent;
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
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;

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

        token = TokenManager.getToken(this);
        contenidoId = getIntent().getLongExtra("contenidoId", -1);

        if (contenidoId == -1) {
            ToastTrackify.mostrar(this, getString(R.string.contenido_no_valido));
            finish();
            return;
        }

        btnEditar.setOnClickListener(v -> abrirEditar());
        btnEliminar.setOnClickListener(v -> confirmarEliminar());
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
                    ToastTrackify.mostrar(DetalleActivity.this, getString(R.string.error_leyendo_contenido));
                    finish();
                    return;
                }

                pintarDatos();
            }

            @Override
            public void onError(UtilREST.Response r) {
                ToastTrackify.mostrar(DetalleActivity.this, getString(R.string.error_cargando_contenido));

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

        android.content.SharedPreferences preferences =
                getSharedPreferences(AjustesActivity.PREFS_NAME, MODE_PRIVATE);

        String valoracionMaxima =
                preferences.getString(
                        AjustesActivity.KEY_VALORACION_MAXIMA,
                        AjustesActivity.VALORACION_5
                );

        if (contenidoActual.getValoracion() != null) {
            int valoracion = contenidoActual.getValoracion();

            if (valoracionMaxima.equals(AjustesActivity.VALORACION_10)) {
                tvValoracionDetalle.setText(
                        getString(R.string.valoracion_formato, String.valueOf(valoracion * 2)) + "/10"
                );
            } else {
                tvValoracionDetalle.setText(
                        getString(R.string.valoracion_formato, String.valueOf(valoracion)) + "/5"
                );
            }
        } else {
            tvValoracionDetalle.setText(getString(R.string.sin_valoracion));
        }

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
                ToastTrackify.mostrar(DetalleActivity.this, getString(R.string.contenido_eliminado));
                finish();
            }

            @Override
            public void onError(UtilREST.Response r) {
                ToastTrackify.mostrar(DetalleActivity.this, getString(R.string.error_eliminando_contenido));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (contenidoActual == null) {
            ToastTrackify.mostrar(DetalleActivity.this, getString(R.string.contenido_no_cargado));
            return true;
        }

        int id = item.getItemId();

        if (id == R.id.menuCompartir) {
            compartirContenido();
            return true;
        }

        if (id == R.id.menuAbrirWeb) {
            abrirWebContenido();
            return true;
        }

        if (id == R.id.menuEnviarEmail) {
            enviarPorEmail();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void compartirContenido() {
        String texto = getString(
                R.string.texto_compartir_contenido,
                contenidoActual.getTitulo(),
                contenidoActual.getTipo(),
                contenidoActual.getGenero(),
                contenidoActual.getEstado(),
                String.valueOf(contenidoActual.getValoracion())
        );

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, texto);

        startActivity(Intent.createChooser(intent, getString(R.string.compartir_con)));
    }

    private void abrirWebContenido() {
        String titulo = contenidoActual.getTitulo();
        String tipo = contenidoActual.getTipo();

        if (titulo == null || titulo.isEmpty()) {
            ToastTrackify.mostrar(DetalleActivity.this, getString(R.string.titulo_vacio));
            return;
        }

        String tituloCodificado = Uri.encode(titulo);
        String url;

        if (tipo != null && (tipo.equalsIgnoreCase("Película") || tipo.equalsIgnoreCase("Pelicula"))) {
            url = "https://www.imdb.com/find/?q=" + tituloCodificado + "&s=tt";

        } else if (tipo != null && tipo.equalsIgnoreCase("Serie")) {
            url = "https://www.imdb.com/find/?q=" + tituloCodificado + "&s=tt";

        } else if (tipo != null && tipo.equalsIgnoreCase("Videojuego")) {
            url = "https://www.metacritic.com/search/" + tituloCodificado + "/";

        } else {
            url = "https://www.google.com/search?q=" + Uri.encode(titulo + " " + tipo);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void enviarPorEmail() {
        String asunto = getString(
                R.string.asunto_email_contenido,
                contenidoActual.getTitulo()
        );

        String cuerpo = getString(
                R.string.cuerpo_email_contenido,
                contenidoActual.getTitulo(),
                contenidoActual.getTipo(),
                contenidoActual.getGenero(),
                contenidoActual.getEstado(),
                String.valueOf(contenidoActual.getValoracion())
        );

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        intent.putExtra(Intent.EXTRA_TEXT, cuerpo);

        startActivity(Intent.createChooser(intent, getString(R.string.enviar_email_chooser)));
    }
}