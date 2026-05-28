package com.example.trackify.controller;

import com.example.trackify.dto.Genero.GeneroDetalleDTO;
import com.example.trackify.service.genero.IGeneroService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeneroControllerTest {

    @Mock
    private IGeneroService generoService;

    @InjectMocks
    private GeneroController generoController;

    @Test
    void listarDebeDevolverGeneros() {
        when(generoService.listarTodos()).thenReturn(List.of(new GeneroDetalleDTO("Acción")));

        ResponseEntity<List<GeneroDetalleDTO>> response = generoController.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Acción", response.getBody().get(0).getNombre());
        verify(generoService).listarTodos();
    }

    @Test
    void obtenerDebeDevolverGeneroPorId() {
        when(generoService.obtenerPorId(1L)).thenReturn(new GeneroDetalleDTO("Acción"));

        ResponseEntity<GeneroDetalleDTO> response = generoController.obtener(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Acción", response.getBody().getNombre());
        verify(generoService).obtenerPorId(1L);
    }
}
