package com.example.trackify.controller;

import com.example.trackify.dto.Tipo.TipoDetalleDTO;
import com.example.trackify.service.tipo.ITipoService;
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
class TipoControllerTest {

    @Mock
    private ITipoService tipoService;

    @InjectMocks
    private TipoController tipoController;

    @Test
    void listarDebeDevolverTipos() {
        when(tipoService.listarTodos()).thenReturn(List.of(new TipoDetalleDTO("Película")));

        ResponseEntity<List<TipoDetalleDTO>> response = tipoController.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Película", response.getBody().get(0).getNombre());
        verify(tipoService).listarTodos();
    }

    @Test
    void obtenerDebeDevolverTipoPorId() {
        when(tipoService.obtenerPorId(1L)).thenReturn(new TipoDetalleDTO("Película"));

        ResponseEntity<TipoDetalleDTO> response = tipoController.obtener(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Película", response.getBody().getNombre());
        verify(tipoService).obtenerPorId(1L);
    }
}
