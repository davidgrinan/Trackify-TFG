package com.example.trackify.service.contenido;

import com.example.trackify.dto.Contenido.ContenidoDTO;
import com.example.trackify.dto.Contenido.RequestContenidoDTO;

import java.util.List;

public interface IContenidoService {

    ContenidoDTO crear(Long usuarioId, RequestContenidoDTO dto);

    ContenidoDTO obtenerPorId(Long id);

    List<ContenidoDTO> listarPorUsuario(Long usuarioId);

    ContenidoDTO actualizar(Long id, RequestContenidoDTO dto);

    void eliminar(Long id);

    List<ContenidoDTO> filtrar(Long usuarioId,
                               String tipo,
                               String genero,
                               String estado,
                               Integer valoracion,
                               String titulo);
}