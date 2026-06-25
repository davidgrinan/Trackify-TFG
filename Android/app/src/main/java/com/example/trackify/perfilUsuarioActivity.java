package com.example.trackify;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONObject;

import API.API;
import API.TokenManager;
import API.UtilJSONParser;
import API.UtilREST;

public class perfilUsuarioActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "trackify_channel";

    private TextView tvNombreUsuario;
    private TextView tvEditarPerfil;
    private TextView tvCambiarPassword;
    private TextView tvAjustes;
    private TextView tvCerrarSesion;

    private String token;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvEditarPerfil = findViewById(R.id.tvEditarPerfil);
        tvCambiarPassword = findViewById(R.id.tvCambiarPassword);
        tvAjustes = findViewById(R.id.tvAjustes);
        tvCerrarSesion = findViewById(R.id.tvCerrarSesion);

        token = TokenManager.getToken(this);
        username = TokenManager.getUsername(this);

        tvNombreUsuario.setText(username);

        crearCanalNotificaciones();

        tvEditarPerfil.setOnClickListener(v ->
                ToastTrackify.mostrar(
                        this,
                        "Funcionalidad disponible próximamente"
                )
        );

        tvCambiarPassword.setOnClickListener(v -> mostrarDialogoCambiarPassword());

        tvAjustes.setOnClickListener(v -> {
            Intent intent = new Intent(perfilUsuarioActivity.this, AjustesActivity.class);
            startActivity(intent);
        });

        tvCerrarSesion.setOnClickListener(v -> confirmarCerrarSesion());
    }
    private void mostrarDialogoCambiarPassword() {
        EditText inputPassword = new EditText(this);
        inputPassword.setHint("Nueva contraseña");
        inputPassword.setPadding(40, 20, 40, 20);

        new AlertDialog.Builder(this)
                .setTitle("Cambiar contraseña")
                .setView(inputPassword)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nuevaPassword = inputPassword.getText().toString().trim();

                    if (nuevaPassword.isEmpty()) {
                        ToastTrackify.mostrar(perfilUsuarioActivity.this, "Introduce una contraseña");
                        return;
                    }

                    cambiarPassword(nuevaPassword);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void cambiarPassword(String nuevaPassword) {
        JSONObject json = UtilJSONParser.createCambiarPassword(nuevaPassword);

        API.cambiarPassword(json, token, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                ToastTrackify.mostrar(
                        perfilUsuarioActivity.this,
                        "Contraseña cambiada correctamente"
                );

                mostrarNotificacionCambioPassword();
            }

            @Override
            public void onError(UtilREST.Response r) {
                ToastTrackify.mostrar(
                        perfilUsuarioActivity.this,
                        "Error cambiando contraseña"
                );
            }
        });
    }

    private void mostrarNotificacionCambioPassword() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo_trackify)
                        .setContentTitle("Cambio de contraseña")
                        .setContentText("La contraseña de usuario " + username + " se ha cambiado correctamente")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    100
            );

            return;
        }

        NotificationManagerCompat.from(this).notify(1, builder.build());
    }

    private void crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Trackify",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void confirmarCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Seguro que quieres cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> cerrarSesion())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void cerrarSesion() {
        TokenManager.clearToken(this);

        ToastTrackify.mostrar(perfilUsuarioActivity.this, "Sesión cerrada");

        Intent intent = new Intent(perfilUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}