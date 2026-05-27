package com.example.trackify.service.auth;

import com.example.trackify.dto.Usuario.CrearUsuarioDTO;
import com.example.trackify.dto.Usuario.LoginUsuarioDTO;

public interface IAuthService {
    //AuthResponseDTO login(LoginUsuarioDTO dto);

    void register(CrearUsuarioDTO dto);
}
