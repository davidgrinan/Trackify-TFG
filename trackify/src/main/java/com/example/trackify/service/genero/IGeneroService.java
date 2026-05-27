package com.example.trackify.service.genero;

import com.example.trackify.dto.Genero.GeneroDetalleDTO;

import java.util.List;

public interface IGeneroService {
    List<GeneroDetalleDTO> listarTodos();

    GeneroDetalleDTO obtenerPorId(Long id);
}
