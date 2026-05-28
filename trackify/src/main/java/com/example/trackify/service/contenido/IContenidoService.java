package com.example.trackify.service.contenido;

import com.example.trackify.dto.Contenido.ContenidoDTO;
import com.example.trackify.dto.Contenido.RequestContenidoDTO;

import java.util.List;

public interface IContenidoService {

    ContenidoDTO crear(String username, RequestContenidoDTO dto);

    ContenidoDTO obtenerPorId(String username, Long id);

    List<ContenidoDTO> listarPorUsuario(String username);

    ContenidoDTO actualizar(String username, Long id, RequestContenidoDTO dto);

    void eliminar(String username, Long id);

    List<ContenidoDTO> filtrar(String username,
                               String tipo,
                               String genero,
                               String estado,
                               Integer valoracion,
                               String titulo);
}