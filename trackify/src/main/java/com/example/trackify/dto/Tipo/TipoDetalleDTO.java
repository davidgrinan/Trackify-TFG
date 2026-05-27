package com.example.trackify.dto.Tipo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TipoDetalleDTO implements Serializable {
    private String nombre;
}