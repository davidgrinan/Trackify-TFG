package com.example.trackify.dto.Genero;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GeneroDetalleDTO implements Serializable {
    String nombre;
}