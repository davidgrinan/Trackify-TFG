package com.example.trackify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import API.API;
import API.ContenidoModel;
import API.TokenManager;
import API.UtilJSONParser;
import API.UtilREST;

import java.util.ArrayList;
import java.util.List;

public class ListadoActivity extends AppCompatActivity {

    private TextView tvTituloCategoria;
    private ImageButton btnFiltros;
    private ImageButton btnAdd;
    private ListView listViewContenido;
    private Button btnVolver;
    private List<ContenidoModel> listaContenidos;
    private ContenidoAdapter adapter;

    private String tipoSeleccionado;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        tvTituloCategoria = findViewById(R.id.tvTituloCategoria);
        btnFiltros = findViewById(R.id.btnFiltros);
        btnAdd = findViewById(R.id.btnAdd);
        btnVolver = findViewById(R.id.btnVolver);
        listViewContenido = findViewById(R.id.listViewContenidos);

        token = TokenManager.getToken(this);
        tipoSeleccionado = getIntent().getStringExtra("tipo");

        if (tipoSeleccionado == null || tipoSeleccionado.isEmpty()) {
            tipoSeleccionado = "Pelicula";
        }

        tvTituloCategoria.setText(tipoSeleccionado);

        listaContenidos = new ArrayList<>();
        adapter = new ContenidoAdapter(this, listaContenidos);
        listViewContenido.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ListadoActivity.this, FormularioActivity.class);
            intent.putExtra("modo", "crear");
            intent.putExtra("tipo", tipoSeleccionado);
            startActivity(intent);
        });

        btnFiltros.setOnClickListener(v -> {
            Toast.makeText(this, "Filtros próximamente", Toast.LENGTH_SHORT).show();
        });

        btnVolver.setOnClickListener(v -> {
            finish();
        });

        listViewContenido.setOnItemClickListener((parent, view, position, id) -> {
            ContenidoModel contenido = listaContenidos.get(position);

            Intent intent = new Intent(ListadoActivity.this, DetalleActivity.class);
            intent.putExtra("contenidoId", contenido.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarContenido();
    }

    private void cargarContenido() {
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Sesión caducada", Toast.LENGTH_SHORT).show();
            volverLogin();
            return;
        }

        API.filtrarContenido(
                tipoSeleccionado,
                null,
                null,
                null,
                null,
                token,
                new UtilREST.OnResponseListener() {
                    @Override
                    public void onSuccess(UtilREST.Response r) {
                        List<ContenidoModel> contenidos =
                                UtilJSONParser.parseArrayContenidos(r.content);

                        listaContenidos.clear();
                        listaContenidos.addAll(contenidos);
                        adapter.notifyDataSetChanged();

                        if (listaContenidos.isEmpty()) {
                            Toast.makeText(
                                    ListadoActivity.this,
                                    "No hay contenido de tipo " + tipoSeleccionado,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onError(UtilREST.Response r) {
                        Toast.makeText(
                                ListadoActivity.this,
                                "Error cargando contenido",
                                Toast.LENGTH_SHORT
                        ).show();

                        if (r.responseCode == 401) {
                            TokenManager.clearToken(ListadoActivity.this);
                            volverLogin();
                        }
                    }
                }
        );
    }

    private void volverLogin() {
        Intent intent = new Intent(ListadoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}