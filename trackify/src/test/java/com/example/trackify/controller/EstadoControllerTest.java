package com.example.trackify.controller;

import com.example.trackify.dto.Estado.EstadoDetalleDTO;
import com.example.trackify.service.estado.IEstadoService;
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
class EstadoControllerTest {

    @Mock
    private IEstadoService estadoService;

    @InjectMocks
    private EstadoController estadoController;

    @Test
    void listarDebeDevolverEstados() {
        when(estadoService.listarTodos()).thenReturn(List.of(new EstadoDetalleDTO("Visto")));

        ResponseEntity<List<EstadoDetalleDTO>> response = estadoController.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Visto", response.getBody().get(0).getNombre());
        verify(estadoService).listarTodos();
    }

    @Test
    void obtenerDebeDevolverEstadoPorId() {
        when(estadoService.obtenerPorId(1L)).thenReturn(new EstadoDetalleDTO("Visto"));

        ResponseEntity<EstadoDetalleDTO> response = estadoController.obtener(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Visto", response.getBody().getNombre());
        verify(estadoService).obtenerPorId(1L);
    }
}
