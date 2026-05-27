package com.example.trackify.service.estado;

import com.example.trackify.dto.Estado.EstadoDetalleDTO;

import java.util.List;

public interface IEstadoService {
    List<EstadoDetalleDTO> listarTodos();

    EstadoDetalleDTO obtenerPorId(Long id);
}
