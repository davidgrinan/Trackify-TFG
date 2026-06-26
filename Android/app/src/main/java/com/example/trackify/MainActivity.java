package com.example.trackify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.PopupMenu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import API.TokenManager;

public class MainActivity extends AppCompatActivity {

    private ImageView btnMenu;
    private ImageView btnPerfil;

    private CardView cardPeliculas;
    private CardView cardSeries;
    private CardView cardVideojuegos;
    private CardView cardSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMenu = findViewById(R.id.btnMenu);
        btnPerfil = findViewById(R.id.btnPerfil);

        cardPeliculas = findViewById(R.id.cardPeliculas);
        cardSeries = findViewById(R.id.cardSeries);
        cardVideojuegos = findViewById(R.id.cardVideojuegos);
        cardSalir = findViewById(R.id.cardSalir);

        btnMenu.setOnClickListener(v -> mostrarMenu());

        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, perfilUsuarioActivity.class);
            startActivity(intent);
        });

        cardPeliculas.setOnClickListener(v -> abrirListado("Pelicula"));

        cardSeries.setOnClickListener(v -> abrirListado("Serie"));

        cardVideojuegos.setOnClickListener(v -> abrirListado("Videojuego"));

        cardSalir.setOnClickListener(v -> cerrarSesion());
    }

    private void abrirListado(String tipo) {
        Intent intent = new Intent(MainActivity.this, ListadoActivity.class);
        intent.putExtra("tipo", tipo);
        startActivity(intent);
    }

    private void cerrarSesion() {
        TokenManager.clearToken(this);

        ToastTrackify.mostrar(this, getString(R.string.sesion_cerrada));

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void mostrarMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnMenu);

        popupMenu.getMenu().add(getString(R.string.inicio));
        popupMenu.getMenu().add(getString(R.string.perfil));
        popupMenu.getMenu().add(getString(R.string.acerca_de));
        popupMenu.getMenu().add(getString(R.string.cerrar_sesion));

        popupMenu.setOnMenuItemClickListener(item -> {
            String opcion = item.getTitle().toString();

            if (opcion.equals(getString(R.string.inicio))) {
                ToastTrackify.mostrar(this, getString(R.string.ya_estas_inicio));
                return true;
            }

            if (opcion.equals(getString(R.string.perfil))) {
                startActivity(new Intent(this, perfilUsuarioActivity.class));
                return true;
            }

            if (opcion.equals(getString(R.string.acerca_de))) {
                startActivity(new Intent(this, AcercaDeActivity.class));
                return true;
            }

            if (opcion.equals(getString(R.string.cerrar_sesion))) {
                cerrarSesion();
                return true;
            }

            return false;
        });

        popupMenu.show();
    }
}