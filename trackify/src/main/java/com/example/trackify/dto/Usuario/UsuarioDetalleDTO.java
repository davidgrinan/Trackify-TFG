package com.example.trackify.dto.Usuario;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.example.trackify.entity.Usuario}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioDetalleDTO implements Serializable {
    private String nombreUsuario;
    private String email;
    private LocalDateTime fechaCreacion;
}