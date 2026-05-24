package com.example.trackify.dto.Tipo;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.example.trackify.entity.Tipo}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TipoDetalleDTO implements Serializable {
    private String nombre;
}