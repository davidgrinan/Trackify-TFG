package com.example.trackify.service.usuario;

import com.example.trackify.exceptions.ErrorGenericoException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUsuarioService extends UserDetailsService {
    public void cambiarPassword(String NombreUsuario, String nuevaPassword);
    public void validarPassword(String password);
}