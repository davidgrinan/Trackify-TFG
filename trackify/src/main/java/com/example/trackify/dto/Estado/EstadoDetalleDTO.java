package com.example.trackify.dto.Estado;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EstadoDetalleDTO implements Serializable {
    private String nombre;
}