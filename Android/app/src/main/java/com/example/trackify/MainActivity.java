package com.example.trackify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

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

        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void mostrarMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnMenu);

        popupMenu.getMenu().add("Inicio");
        popupMenu.getMenu().add("Perfil");
        popupMenu.getMenu().add("Acerca de");
        popupMenu.getMenu().add("Cerrar sesión");

        popupMenu.setOnMenuItemClickListener(item -> {
            String opcion = item.getTitle().toString();

            if (opcion.equals("Inicio")) {
                Toast.makeText(this, "Ya estás en inicio", Toast.LENGTH_SHORT).show();
                return true;
            }

            if (opcion.equals("Perfil")) {
                startActivity(new Intent(this, perfilUsuarioActivity.class));
                return true;
            }

            if (opcion.equals("Acerca de")) {
                startActivity(new Intent(this, AcercaDeActivity.class));
                return true;
            }

            if (opcion.equals("Cerrar sesión")) {
                cerrarSesion();
                return true;
            }

            return false;
        });

        popupMenu.show();
    }
}