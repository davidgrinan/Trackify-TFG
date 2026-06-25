package com.example.trackify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import API.API;
import API.TokenManager;
import API.UtilJSONParser;
import API.UtilREST;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsuario;
    private TextInputEditText etPassword;
    private MaterialButton btnLogin;
    private TextView tvRegistrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegistrate = findViewById(R.id.tvRegistrate);

        btnLogin.setOnClickListener(v -> login());

        tvRegistrate.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        String usuario = etUsuario.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (usuario.isEmpty()) {
            etUsuario.setError("Introduce el usuario");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Introduce la contraseña");
            return;
        }

        JSONObject jsonLogin = UtilJSONParser.createLogin(usuario, password);

        API.login(jsonLogin, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                String token = UtilJSONParser.parseToken(r.content);

                if (token == null || token.isEmpty()) {
                    ToastTrackify.mostrar(LoginActivity.this, "Error al iniciar sesión");
                    return;
                }

                TokenManager.saveToken(LoginActivity.this, token);
                TokenManager.saveUsername(LoginActivity.this, usuario);

                ToastTrackify.mostrar(LoginActivity.this, "Login correcto");

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(UtilREST.Response r) {
                ToastTrackify.mostrar(LoginActivity.this, "Usuario o contraseña incorrectos");
            }
        });
    }


}