package com.example.trackify.service.tipo;

import com.example.trackify.dto.Tipo.*;

import java.util.List;

public interface ITipoService {

    List<TipoDetalleDTO> listarTodos();

    TipoDetalleDTO obtenerPorId(Long id);
}