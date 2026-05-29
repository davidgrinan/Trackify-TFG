package com.example.trackify.dto.Contenido;

import com.example.trackify.dto.Estado.EstadoDetalleDTO;
import com.example.trackify.dto.Genero.GeneroDetalleDTO;
import com.example.trackify.dto.Tipo.TipoDetalleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContenidoDTO implements Serializable {
    private long id;
    private String imagenUrl;
    private String titulo;
    private Integer valoracion;
    private String descripcion;
    private GeneroDetalleDTO genero;
    private TipoDetalleDTO tipo;
    private EstadoDetalleDTO estado;
}
