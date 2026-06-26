package com.example.trackify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import API.API;
import API.ContenidoModel;
import API.TokenManager;
import API.UtilJSONParser;
import API.UtilREST;

import java.util.ArrayList;
import java.util.Collections;
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

        pintarTituloCategoria();

        listaContenidos = new ArrayList<>();
        adapter = new ContenidoAdapter(this, listaContenidos);
        listViewContenido.setAdapter(adapter);

        registerForContextMenu(listViewContenido);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ListadoActivity.this, FormularioActivity.class);
            intent.putExtra("modo", "crear");
            intent.putExtra("tipo", tipoSeleccionado);
            startActivity(intent);
        });

        btnFiltros.setOnClickListener(v -> mostrarDialogoFiltros());

        btnVolver.setOnClickListener(v -> finish());

        listViewContenido.setOnItemClickListener((parent, view, position, id) -> {
            ContenidoModel contenido = listaContenidos.get(position);

            Intent intent = new Intent(ListadoActivity.this, DetalleActivity.class);
            intent.putExtra("contenidoId", contenido.getId());
            startActivity(intent);
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listViewContenidos) {
            getMenuInflater().inflate(R.menu.menu_detalle, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (info == null) {
            return super.onContextItemSelected(item);
        }

        ContenidoModel contenido = listaContenidos.get(info.position);

        int id = item.getItemId();

        if (id == R.id.menuCompartir) {
            compartirContenido(contenido);
            return true;
        }

        if (id == R.id.menuAbrirWeb) {
            abrirWebContenido(contenido);
            return true;
        }

        if (id == R.id.menuEnviarEmail) {
            enviarPorEmail(contenido);
            return true;
        }

        return super.onContextItemSelected(item);
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

        adapterGeneros.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spFiltroGenero.setAdapter(adapterGeneros);

        ArrayAdapter<CharSequence> adapterEstados =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.estados_filtro_array,
                        android.R.layout.simple_spinner_item
                );

        adapterEstados.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spFiltroEstado.setAdapter(adapterEstados);

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.filtrar_contenidos))
                .setView(view)
                .setPositiveButton(getString(R.string.filtrar), (dialog, which) -> {
                    String genero = spFiltroGenero.getSelectedItem().toString();
                    String estado = spFiltroEstado.getSelectedItem().toString();
                    String titulo = etFiltroTitulo.getText().toString().trim();

                    Integer valoracion = null;

                    String valoracionTexto = etFiltroValoracion.getText().toString().trim();

                    if (!valoracionTexto.isEmpty()) {
                        try {
                            valoracion = Integer.parseInt(valoracionTexto);
                        } catch (NumberFormatException e) {
                            ToastTrackify.mostrar(
                                    ListadoActivity.this,
                                    getString(R.string.valoracion_no_valida)
                            );
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
                .setNegativeButton(getString(R.string.cancelar), null)
                .setNeutralButton(getString(R.string.quitar_filtros), (dialog, which) -> cargarContenido())
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
                        aplicarOrdenPreferido();
                        adapter.notifyDataSetChanged();

                        if (listaContenidos.isEmpty()) {
                            ToastTrackify.mostrar(
                                    ListadoActivity.this,
                                    getString(R.string.sin_resultados_filtros)
                            );
                        }
                    }

                    @Override
                    public void onError(UtilREST.Response r) {
                        ToastTrackify.mostrar(
                                ListadoActivity.this,
                                getString(R.string.error_filtros)
                        );
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
            ToastTrackify.mostrar(this, getString(R.string.sesion_caducada));
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
                        aplicarOrdenPreferido();
                        adapter.notifyDataSetChanged();

                        if (listaContenidos.isEmpty()) {
                            ToastTrackify.mostrar(
                                    ListadoActivity.this,
                                    getString(R.string.no_hay_contenido_tipo, tipoSeleccionado)
                            );
                        }
                    }

                    @Override
                    public void onError(UtilREST.Response r) {
                        ToastTrackify.mostrar(
                                ListadoActivity.this,
                                getString(R.string.no_se_pudo_cargar_contenido)
                        );

                        if (r.responseCode == 401) {
                            TokenManager.clearToken(ListadoActivity.this);
                            volverLogin();
                        }
                    }
                }
        );
    }

    private void aplicarOrdenPreferido() {
        android.content.SharedPreferences preferences =
                getSharedPreferences(AjustesActivity.PREFS_NAME, MODE_PRIVATE);

        String orden =
                preferences.getString(
                        AjustesActivity.KEY_ORDEN_LISTADO,
                        AjustesActivity.ORDEN_TITULO
                );

        if (orden.equals(AjustesActivity.ORDEN_TITULO)) {
            Collections.sort(listaContenidos, (c1, c2) ->
                    c1.getTitulo().compareToIgnoreCase(c2.getTitulo())
            );
        }

        if (orden.equals(AjustesActivity.ORDEN_VALORACION)) {
            Collections.sort(listaContenidos, (c1, c2) -> {
                int v1 = c1.getValoracion() == null ? 0 : c1.getValoracion();
                int v2 = c2.getValoracion() == null ? 0 : c2.getValoracion();

                return Integer.compare(v2, v1);
            });
        }

        if (orden.equals(AjustesActivity.ORDEN_ESTADO)) {
            Collections.sort(listaContenidos, (c1, c2) ->
                    c1.getEstado().compareToIgnoreCase(c2.getEstado())
            );
        }
    }

    private void pintarTituloCategoria() {
        if (tipoSeleccionado.equalsIgnoreCase("Pelicula")
                || tipoSeleccionado.equalsIgnoreCase("Película")) {

            tvTituloCategoria.setText(getString(R.string.peliculas));

        } else if (tipoSeleccionado.equalsIgnoreCase("Serie")) {

            tvTituloCategoria.setText(getString(R.string.series));

        } else if (tipoSeleccionado.equalsIgnoreCase("Videojuego")) {

            tvTituloCategoria.setText(getString(R.string.videojuegos));
        }
    }

    private void compartirContenido(ContenidoModel contenido) {
        String texto = getString(
                R.string.texto_compartir_contenido,
                contenido.getTitulo(),
                contenido.getTipo(),
                contenido.getGenero(),
                contenido.getEstado(),
                String.valueOf(contenido.getValoracion())
        );

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, texto);

        startActivity(Intent.createChooser(intent, getString(R.string.compartir_con)));
    }

    private void abrirWebContenido(ContenidoModel contenido) {
        String titulo = contenido.getTitulo();
        String tipo = contenido.getTipo();

        if (titulo == null || titulo.isEmpty()) {
            ToastTrackify.mostrar(
                    ListadoActivity.this,
                    getString(R.string.titulo_vacio)
            );
            return;
        }

        String tituloCodificado = Uri.encode(titulo);
        String url;

        if (tipo != null && (tipo.equalsIgnoreCase("Película")
                || tipo.equalsIgnoreCase("Pelicula"))) {

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

    private void enviarPorEmail(ContenidoModel contenido) {
        String asunto = getString(
                R.string.asunto_email_contenido,
                contenido.getTitulo()
        );

        String cuerpo = getString(
                R.string.cuerpo_email_contenido,
                contenido.getTitulo(),
                contenido.getTipo(),
                contenido.getGenero(),
                contenido.getEstado(),
                String.valueOf(contenido.getValoracion())
        );

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        intent.putExtra(Intent.EXTRA_TEXT, cuerpo);

        startActivity(Intent.createChooser(intent, getString(R.string.enviar_email_chooser)));
    }

    private void volverLogin() {
        Intent intent = new Intent(ListadoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}