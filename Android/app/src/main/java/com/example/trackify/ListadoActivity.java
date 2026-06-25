package com.example.trackify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

        btnFiltros.setOnClickListener(v -> mostrarDialogoFiltros());

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

    private void mostrarDialogoFiltros() {
        View view = getLayoutInflater().inflate(R.layout.dialog_filtros, null);

        Spinner spFiltroGenero = view.findViewById(R.id.spFiltroGenero);
        Spinner spFiltroEstado = view.findViewById(R.id.spFiltroEstado);
        EditText etFiltroValoracion = view.findViewById(R.id.etFiltroValoracion);
        EditText etFiltroTitulo = view.findViewById(R.id.etFiltroTitulo);

        ArrayAdapter<CharSequence> adapterGeneros =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.generos_filtro_array,
                        android.R.layout.simple_spinner_item
                );

        adapterGeneros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFiltroGenero.setAdapter(adapterGeneros);

        ArrayAdapter<CharSequence> adapterEstados =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.estados_filtro_array,
                        android.R.layout.simple_spinner_item
                );

        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFiltroEstado.setAdapter(adapterEstados);

        new AlertDialog.Builder(this)
                .setTitle("Filtrar contenidos")
                .setView(view)
                .setPositiveButton("Filtrar", (dialog, which) -> {
                    String genero = spFiltroGenero.getSelectedItem().toString();
                    String estado = spFiltroEstado.getSelectedItem().toString();
                    String titulo = etFiltroTitulo.getText().toString().trim();

                    Integer valoracion = null;

                    String valoracionTexto = etFiltroValoracion.getText().toString().trim();

                    if (!valoracionTexto.isEmpty()) {
                        try {
                            valoracion = Integer.parseInt(valoracionTexto);
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "Valoración no válida", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    if (genero.equals("Todos")) {
                        genero = null;
                    }

                    if (estado.equals("Todos")) {
                        estado = null;
                    }

                    if (titulo.isEmpty()) {
                        titulo = null;
                    }

                    cargarContenidoFiltrado(genero, estado, valoracion, titulo);
                })
                .setNegativeButton("Cancelar", null)
                .setNeutralButton("Quitar filtros", (dialog, which) -> cargarContenido())
                .show();
    }

    private void cargarContenidoFiltrado(String genero,
                                         String estado,
                                         Integer valoracion,
                                         String titulo) {

        API.filtrarContenido(
                tipoSeleccionado,
                genero,
                estado,
                valoracion,
                titulo,
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
                                    "No hay resultados con esos filtros",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onError(UtilREST.Response r) {
                        Toast.makeText(
                                ListadoActivity.this,
                                "Error aplicando filtros",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
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