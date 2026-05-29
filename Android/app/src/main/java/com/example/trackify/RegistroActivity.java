package com.example.trackify;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
        String password = etRegistroPassword.getText().toString().trim();
        String passwordConfirm = etRegistroPasswordConfirm.getText().toString().trim();

        if (usuario.isEmpty()) {
            etRegistroUsuario.setError("Introduce el usuario");
            return;
        }

        if (email.isEmpty()) {
            etRegistroEmail.setError("Introduce el email");
            return;
        }

        if (password.isEmpty()) {
            etRegistroPassword.setError("Introduce la contraseña");
            return;
        }

        if (!password.equals(passwordConfirm)) {
            etRegistroPasswordConfirm.setError("Las contraseñas no coinciden");
            return;
        }

        JSONObject jsonRegistro = UtilJSONParser.createRegister(usuario, password, email);

        API.register(jsonRegistro, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                mostrarToastPersonalizado("Usuario registrado");

                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(UtilREST.Response r) {
                mostrarToastPersonalizado("Error registrando usuario");
            }
        });
    }

    private void mostrarToastPersonalizado(String mensaje) {
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(
                R.layout.toast_trackify,
                findViewById(android.R.id.content),
                false
        );

        TextView texto = layout.findViewById(R.id.txtToast);
        texto.setText(mensaje);

        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}