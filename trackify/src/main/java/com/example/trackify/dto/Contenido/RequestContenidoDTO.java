package com.example.trackify.dto.Contenido;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestContenidoDTO implements Serializable {

    @NotBlank(message = "El titulo no puede estar vacio")
    @Size(min = 2, max = 100, message = "El titulo debe tener entre 2 y 100 caracteres")
    private String titulo;

    @Min(value = 0, message = "La valoracion minima es 0")
    @Max(value = 5, message = "La valoracion maxima es 5")
    private Integer valoracion;

    @Size(max = 500, message = "La descripcion no puede superar los 500 caracteres")
    private String descripcion;

    @NotBlank(message = "El genero es obligatorio")
    private String genero;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}