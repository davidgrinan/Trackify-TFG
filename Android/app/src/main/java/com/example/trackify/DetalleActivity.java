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
            Toast.makeText(this, "Contenido no válido", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DetalleActivity.this, "Error leyendo contenido", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                pintarDatos();
            }

            @Override
            public void onError(UtilREST.Response r) {
                Toast.makeText(DetalleActivity.this, "Error cargando detalle", Toast.LENGTH_SHORT).show();

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
            tvValoracionDetalle.setText("⭐ Valoración: " + contenidoActual.getValoracion() + "/10");
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
                Toast.makeText(DetalleActivity.this, "Contenido eliminado", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(UtilREST.Response r) {
                Toast.makeText(DetalleActivity.this, "Error eliminando contenido", Toast.LENGTH_SHORT).show();
            }
        });
    }
}