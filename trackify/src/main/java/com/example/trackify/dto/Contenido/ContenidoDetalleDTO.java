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
public class ContenidoDetalleDTO implements Serializable {
    private long id;
    private String titulo;
    private Integer valoracion;
    private String descripcion;}
