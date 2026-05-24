package com.example.trackify.dto.Usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CrearUsuarioDTO implements Serializable {
    private String nombreUsuario;
    private String password;
    private String email;
    public List<Integer> roles;
}
