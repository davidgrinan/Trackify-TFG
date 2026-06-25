package com.example.trackify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
            ToastTrackify.mostrar(this, "Contenido no válido");
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
                    ToastTrackify.mostrar(DetalleActivity.this, "Error leyendo contenido");
                    finish();
                    return;
                }

                pintarDatos();
            }

            @Override
            public void onError(UtilREST.Response r) {
                ToastTrackify.mostrar(DetalleActivity.this, "Error al cargar contenido");

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
        tvTipoDetalle.setText("Tipo: " + contenidoActual.getTipo());
        tvGeneroDetalle.setText("Género: " + contenidoActual.getGenero());
        tvEstadoDetalle.setText("Estado: " + contenidoActual.getEstado());
        tvDescripcionDetalle.setText(contenidoActual.getDescripcion());

        if (contenidoActual.getValoracion() != null) {
            tvValoracionDetalle.setText("⭐ Valoración: " + contenidoActual.getValoracion() + "/5");
        } else {
            tvValoracionDetalle.setText("⭐ Sin valoración");
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
                .setTitle("Eliminar contenido")
                .setMessage("¿Seguro que quieres eliminar este contenido?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarContenido())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarContenido() {
        API.eliminarContenido(contenidoId, token, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                ToastTrackify.mostrar(DetalleActivity.this, "Contenido eliminado");
                finish();
            }

            @Override
            public void onError(UtilREST.Response r) {
                ToastTrackify.mostrar(DetalleActivity.this, "Error eliminando contenido");
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
            ToastTrackify.mostrar(DetalleActivity.this, "Contenido no cargado");
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
        String texto = "Te recomiendo este contenido de Trackify:\n\n"
                + contenidoActual.getTitulo()
                + "\nTipo: " + contenidoActual.getTipo()
                + "\nGénero: " + contenidoActual.getGenero()
                + "\nEstado: " + contenidoActual.getEstado()
                + "\nValoración: " + contenidoActual.getValoracion() + "/5";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, texto);

        startActivity(Intent.createChooser(intent, "Compartir con"));
    }

    private void abrirWebContenido() {
        String titulo = contenidoActual.getTitulo();
        String tipo = contenidoActual.getTipo();

        if (titulo == null || titulo.isEmpty()) {
            ToastTrackify.mostrar(DetalleActivity.this, "Este contenido no tiene título");
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
        String asunto = "Contenido recomendado: " + contenidoActual.getTitulo();

        String cuerpo = "Hola,\n\nTe recomiendo este contenido:\n\n"
                + "Título: " + contenidoActual.getTitulo()
                + "\nTipo: " + contenidoActual.getTipo()
                + "\nGénero: " + contenidoActual.getGenero()
                + "\nEstado: " + contenidoActual.getEstado()
                + "\nValoración: " + contenidoActual.getValoracion() + "/5"
                + "\n\nEnviado desde Trackify.";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        intent.putExtra(Intent.EXTRA_TEXT, cuerpo);

        startActivity(Intent.createChooser(intent, "Enviar email"));
    }
}