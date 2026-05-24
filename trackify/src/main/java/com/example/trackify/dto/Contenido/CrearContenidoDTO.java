package com.example.trackify.dto.Contenido;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CrearContenidoDTO implements Serializable {
    private String titulo;
    private Integer valoracion;
    private String descripcion;
    private String genero;
    private String tipo;
    private String estado;
}
