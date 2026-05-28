package com.example.trackify.service;

import com.example.trackify.dto.Genero.GeneroDetalleDTO;
import com.example.trackify.entity.Genero;
import com.example.trackify.exceptions.NotFoundEntityException;
import com.example.trackify.mapper.GeneroMapper;
import com.example.trackify.repository.Genero.IGeneroRepository;
import com.example.trackify.service.genero.GeneroService;
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
class GeneroServiceTest {

    @Mock
    private IGeneroRepository generoRepository;

    @Mock
    private GeneroMapper mapper;

    @InjectMocks
    private GeneroService generoService;

    @Test
    void listarTodosDebeDevolverGeneros() {
        Genero genero = new Genero();
        genero.setId(1L);
        genero.setNombre("Acción");
        GeneroDetalleDTO dto = new GeneroDetalleDTO("Acción");

        when(generoRepository.findAll()).thenReturn(List.of(genero));
        when(mapper.toDetalleDTOList(List.of(genero))).thenReturn(List.of(dto));

        List<GeneroDetalleDTO> resultado = generoService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Acción", resultado.get(0).getNombre());
        verify(generoRepository).findAll();
    }

    @Test
    void obtenerPorIdDebeDevolverGenero() {
        Genero genero = new Genero();
        genero.setId(1L);
        genero.setNombre("Acción");
        GeneroDetalleDTO dto = new GeneroDetalleDTO("Acción");

        when(generoRepository.findById(1L)).thenReturn(Optional.of(genero));
        when(mapper.toDetalleDTO(genero)).thenReturn(dto);

        GeneroDetalleDTO resultado = generoService.obtenerPorId(1L);

        assertEquals("Acción", resultado.getNombre());
        verify(generoRepository).findById(1L);
    }

    @Test
    void obtenerPorIdDebeLanzarNotFound() {
        when(generoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> generoService.obtenerPorId(99L));
    }
}
