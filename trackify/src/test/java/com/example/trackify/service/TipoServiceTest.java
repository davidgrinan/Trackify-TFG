package com.example.trackify.service;

import com.example.trackify.dto.Tipo.TipoDetalleDTO;
import com.example.trackify.entity.Tipo;
import com.example.trackify.exceptions.NotFoundEntityException;
import com.example.trackify.mapper.TipoMapper;
import com.example.trackify.repository.Tipo.ITipoRepository;
import com.example.trackify.service.tipo.TipoService;
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
class TipoServiceTest {

    @Mock
    private ITipoRepository tipoRepository;

    @Mock
    private TipoMapper mapper;

    @InjectMocks
    private TipoService tipoService;

    @Test
    void listarTodosDebeDevolverTipos() {
        Tipo tipo = new Tipo();
        tipo.setId(1L);
        tipo.setNombre("Película");
        TipoDetalleDTO dto = new TipoDetalleDTO("Película");

        when(tipoRepository.findAll()).thenReturn(List.of(tipo));
        when(mapper.toDetalleDTOList(List.of(tipo))).thenReturn(List.of(dto));

        List<TipoDetalleDTO> resultado = tipoService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Película", resultado.get(0).getNombre());
        verify(tipoRepository).findAll();
    }

    @Test
    void obtenerPorIdDebeDevolverTipo() {
        Tipo tipo = new Tipo();
        tipo.setId(1L);
        tipo.setNombre("Película");
        TipoDetalleDTO dto = new TipoDetalleDTO("Película");

        when(tipoRepository.findById(1L)).thenReturn(Optional.of(tipo));
        when(mapper.toDetalleDTO(tipo)).thenReturn(dto);

        TipoDetalleDTO resultado = tipoService.obtenerPorId(1L);

        assertEquals("Película", resultado.getNombre());
        verify(tipoRepository).findById(1L);
    }

    @Test
    void obtenerPorIdDebeLanzarNotFound() {
        when(tipoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> tipoService.obtenerPorId(99L));
    }
}
