package com.example.trackify;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONObject;

import API.API;
import API.TokenManager;
import API.UtilJSONParser;
import API.UtilREST;

public class perfilUsuarioActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PASSWORD_ID = 1;
    private static final int REQUEST_NOTIFICATIONS = 100;

    private TextView tvNombreUsuario;
    private TextView tvEditarPerfil;
    private TextView tvAjustes;
    private TextView tvCerrarSesion;

    private Button btnVolver;

    private String token;
    private String username;

    private boolean notificacionPasswordPendiente = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvEditarPerfil = findViewById(R.id.tvEditarPerfil);
        tvAjustes = findViewById(R.id.tvAjustes);
        tvCerrarSesion = findViewById(R.id.tvCerrarSesion);
        btnVolver = findViewById(R.id.btnVolverPerfil);

        token = TokenManager.getToken(this);
        username = TokenManager.getUsername(this);

        if (username != null && !username.isEmpty()) {
            tvNombreUsuario.setText(username);
        }

        NotificacionesHelper.crearCanalNotificaciones(this);

        tvEditarPerfil.setOnClickListener(v -> mostrarDialogoEditarPerfil());

        tvAjustes.setOnClickListener(v -> {
            Intent intent = new Intent(perfilUsuarioActivity.this, AjustesActivity.class);
            startActivity(intent);
        });

        tvCerrarSesion.setOnClickListener(v -> confirmarCerrarSesion());

        btnVolver.setOnClickListener(v -> finish());
    }

    private void mostrarDialogoEditarPerfil() {
        EditText inputPassword = new EditText(this);
        inputPassword.setHint(getString(R.string.nueva_password));
        inputPassword.setPadding(40, 20, 40, 20);

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.editar_perfil_opcion))
                .setMessage(getString(R.string.cambiar_password))
                .setView(inputPassword)
                .setPositiveButton(getString(R.string.guardar), (dialog, which) -> {
                    String nuevaPassword = inputPassword.getText().toString();

                    if (!validarPasswordAndroid(nuevaPassword)) {
                        return;
                    }

                    cambiarPassword(nuevaPassword);
                })
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();
    }

    private boolean validarPasswordAndroid(String password) {
        if (password == null || password.trim().isEmpty()) {
            ToastTrackify.mostrar(
                    perfilUsuarioActivity.this,
                    getString(R.string.introduce_password)
            );
            return false;
        }

        if (password.length() < 8) {
            ToastTrackify.mostrar(
                    perfilUsuarioActivity.this,
                    getString(R.string.password_minimo_8)
            );
            return false;
        }

        return true;
    }

    private void cambiarPassword(String nuevaPassword) {
        JSONObject json = UtilJSONParser.createCambiarPassword(nuevaPassword);

        API.cambiarPassword(json, token, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                ToastTrackify.mostrar(
                        perfilUsuarioActivity.this,
                        getString(R.string.password_cambiada)
                );

                mostrarNotificacionCambioPassword();
            }

            @Override
            public void onError(UtilREST.Response r) {
                String mensajeError = obtenerMensajeErrorAPI(
                        r,
                        getString(R.string.error_password)
                );

                ToastTrackify.mostrar(
                        perfilUsuarioActivity.this,
                        mensajeError
                );
            }
        });
    }

    private void mostrarNotificacionCambioPassword() {
        if (!NotificacionesHelper.tienePermisoNotificaciones(this)) {
            notificacionPasswordPendiente = true;
            NotificacionesHelper.solicitarPermisoNotificaciones(this);
            return;
        }

        lanzarNotificacionCambioPassword();
    }

    @SuppressLint("MissingPermission")
    private void lanzarNotificacionCambioPassword() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, NotificacionesHelper.CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo_trackify)
                        .setContentTitle(getString(R.string.notificacion_cambio_password_titulo))
                        .setContentText(getString(R.string.notificacion_cambio_password_texto, username))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        notificationManager.notify(NOTIFICATION_PASSWORD_ID, builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_NOTIFICATIONS) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                ToastTrackify.mostrar(
                        perfilUsuarioActivity.this,
                        getString(R.string.permiso_notificaciones_aceptado)
                );

                if (notificacionPasswordPendiente) {
                    notificacionPasswordPendiente = false;
                    lanzarNotificacionCambioPassword();
                }

            } else {
                notificacionPasswordPendiente = false;

                ToastTrackify.mostrar(
                        perfilUsuarioActivity.this,
                        getString(R.string.permiso_notificaciones_denegado)
                );
            }
        }
    }

    private String obtenerMensajeErrorAPI(UtilREST.Response r, String mensajePorDefecto) {
        if (r == null || r.content == null || r.content.trim().isEmpty()) {
            return mensajePorDefecto;
        }

        String contenido = r.content.trim();

        try {
            JSONObject jsonError = new JSONObject(contenido);

            if (jsonError.has("message")) {
                String mensaje = jsonError.optString("message");

                if (mensaje != null && !mensaje.trim().isEmpty()) {
                    return mensaje;
                }
            }

            if (jsonError.has("error")) {
                String error = jsonError.optString("error");

                if (error != null && !error.trim().isEmpty()) {
                    return error;
                }
            }

        } catch (Exception e) {
            return limpiarMensajeTextoPlano(contenido);
        }

        return mensajePorDefecto;
    }

    private String limpiarMensajeTextoPlano(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            return getString(R.string.error_password);
        }

        mensaje = mensaje.trim();

        if (mensaje.startsWith("\"") && mensaje.endsWith("\"")) {
            mensaje = mensaje.substring(1, mensaje.length() - 1);
        }

        return mensaje;
    }

    private void confirmarCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.cerrar_sesion))
                .setMessage(getString(R.string.confirmar_cerrar_sesion))
                .setPositiveButton(getString(R.string.si), (dialog, which) -> cerrarSesion())
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();
    }

    private void cerrarSesion() {
        TokenManager.clearToken(this);

        ToastTrackify.mostrar(
                perfilUsuarioActivity.this,
                getString(R.string.sesion_cerrada)
        );

        Intent intent = new Intent(perfilUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}