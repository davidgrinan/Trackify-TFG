package com.example.trackify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import API.API;
import API.UtilJSONParser;
import API.UtilREST;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

public class RegistroActivity extends AppCompatActivity {

    private TextInputEditText etRegistroUsuario;
    private TextInputEditText etRegistroEmail;
    private TextInputEditText etRegistroPassword;
    private TextInputEditText etRegistroPasswordConfirm;
    private MaterialButton btnRegistrar;
    private TextView tvVolverLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etRegistroUsuario = findViewById(R.id.etRegistroUsuario);
        etRegistroEmail = findViewById(R.id.etRegistroEmail);
        etRegistroPassword = findViewById(R.id.etRegistroPassword);
        etRegistroPasswordConfirm = findViewById(R.id.etRegistroPasswordConfirm);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        tvVolverLogin = findViewById(R.id.tvVolverLogin);

        btnRegistrar.setOnClickListener(v -> registrar());

        tvVolverLogin.setOnClickListener(v -> finish());
    }

    private void registrar() {
        String usuario = etRegistroUsuario.getText().toString().trim();
        String email = etRegistroEmail.getText().toString().trim();
        String password = etRegistroPassword.getText().toString();
        String passwordConfirm = etRegistroPasswordConfirm.getText().toString();

        if (usuario.isEmpty()) {
            etRegistroUsuario.setError(getString(R.string.introduce_usuario));
            return;
        }

        if (email.isEmpty()) {
            etRegistroEmail.setError(getString(R.string.introduce_email));
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etRegistroEmail.setError("El email no tiene un formato válido");
            return;
        }

        if (!validarPasswordAndroid(password)) {
            return;
        }

        if (passwordConfirm.trim().isEmpty()) {
            etRegistroPasswordConfirm.setError(getString(R.string.introduce_contrasena));
            return;
        }

        if (!password.equals(passwordConfirm)) {
            etRegistroPasswordConfirm.setError(getString(R.string.passwords_no_coinciden));
            return;
        }

        JSONObject jsonRegistro = UtilJSONParser.createRegister(usuario, password, email);

        API.register(jsonRegistro, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                ToastTrackify.mostrar(
                        RegistroActivity.this,
                        getString(R.string.usuario_registrado)
                );

                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(UtilREST.Response r) {
                ToastTrackify.mostrar(
                        RegistroActivity.this,
                        obtenerMensajeErrorAPI(r, getString(R.string.error_registro))
                );
            }
        });
    }

    private boolean validarPasswordAndroid(String password) {
        if (password == null || password.trim().isEmpty()) {
            etRegistroPassword.setError(getString(R.string.introduce_contrasena));
            return false;
        }

        if (password.length() < 8) {
            etRegistroPassword.setError(getString(R.string.password_minimo_8));
            return false;
        }

        return true;
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
                    return limpiarMensajeError(mensaje);
                }
            }

            if (jsonError.has("error")) {
                String error = jsonError.optString("error");

                if (error != null && !error.trim().isEmpty()) {
                    return limpiarMensajeError(error);
                }
            }

        } catch (Exception e) {
            return limpiarMensajeError(contenido);
        }

        return mensajePorDefecto;
    }

    private String limpiarMensajeError(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            return getString(R.string.error_registro);
        }

        mensaje = mensaje.trim();

        if (mensaje.startsWith("\"") && mensaje.endsWith("\"")) {
            mensaje = mensaje.substring(1, mensaje.length() - 1);
        }

        if (mensaje.startsWith("{") && mensaje.endsWith("}")) {
            mensaje = mensaje.substring(1, mensaje.length() - 1);
        }

        if (mensaje.contains(":")) {
            mensaje = mensaje.substring(mensaje.indexOf(":") + 1).trim();
        }

        mensaje = mensaje.replace("\"", "");
        mensaje = mensaje.replace("\\", "");

        return mensaje;
    }
}