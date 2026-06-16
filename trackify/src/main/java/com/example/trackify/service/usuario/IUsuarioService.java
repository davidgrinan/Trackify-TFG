package com.example.trackify.service.usuario;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUsuarioService extends UserDetailsService {
    public void cambiarPassword(String NombreUsuario, String nuevaPassword);
}