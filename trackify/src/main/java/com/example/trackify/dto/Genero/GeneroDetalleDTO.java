package com.example.trackify.dto.Genero;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.example.trackify.entity.Genero}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GeneroDetalleDTO implements Serializable {
    String nombre;
}