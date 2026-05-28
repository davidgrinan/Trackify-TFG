package com.example.trackify.service;

import com.example.trackify.dto.Estado.EstadoDetalleDTO;
import com.example.trackify.entity.Estado;
import com.example.trackify.exceptions.NotFoundEntityException;
import com.example.trackify.mapper.EstadoMapper;
import com.example.trackify.repository.Estado.IEstadoRepository;
import com.example.trackify.service.estado.EstadoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoServiceTest {

    @Mock
    private IEstadoRepository estadoRepository;

    @Mock
    private EstadoMapper mapper;

    @InjectMocks
    private EstadoService estadoService;

    @Test
    void listarTodosDebeDevolverEstados() {
        Estado estado = new Estado();
        estado.setId(1L);
        estado.setNombre("Visto");
        EstadoDetalleDTO dto = new EstadoDetalleDTO("Visto");

        when(estadoRepository.findAll()).thenReturn(List.of(estado));
        when(mapper.toDetalleDTOList(List.of(estado))).thenReturn(List.of(dto));

        List<EstadoDetalleDTO> resultado = estadoService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Visto", resultado.get(0).getNombre());
        verify(estadoRepository).findAll();
    }

    @Test
    void obtenerPorIdDebeDevolverEstado() {
        Estado estado = new Estado();
        estado.setId(1L);
        estado.setNombre("Visto");
        EstadoDetalleDTO dto = new EstadoDetalleDTO("Visto");

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(mapper.toDetalleDTO(estado)).thenReturn(dto);

        EstadoDetalleDTO resultado = estadoService.obtenerPorId(1L);

        assertEquals("Visto", resultado.getNombre());
        verify(estadoRepository).findById(1L);
    }

    @Test
    void obtenerPorIdDebeLanzarNotFound() {
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> estadoService.obtenerPorId(99L));
    }
}
