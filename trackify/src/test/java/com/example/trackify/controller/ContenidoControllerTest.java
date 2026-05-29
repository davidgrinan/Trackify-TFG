package com.example.trackify.controller;

import com.example.trackify.dto.Contenido.ContenidoDTO;
import com.example.trackify.dto.Contenido.RequestContenidoDTO;
import com.example.trackify.dto.Estado.EstadoDetalleDTO;
import com.example.trackify.dto.Genero.GeneroDetalleDTO;
import com.example.trackify.dto.Tipo.TipoDetalleDTO;
import com.example.trackify.exceptions.Response;
import com.example.trackify.service.contenido.IContenidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContenidoControllerTest {

    @Mock
    private IContenidoService contenidoService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ContenidoController contenidoController;

    private ContenidoDTO contenidoDTO;
    private RequestContenidoDTO requestDTO;

    @BeforeEach
    void setUp() {
        contenidoDTO = new ContenidoDTO();
        contenidoDTO.setId(1L);
        contenidoDTO.setTitulo("Superman");
        contenidoDTO.setValoracion(5);
        contenidoDTO.setDescripcion("Película de superhéroes");
        contenidoDTO.setGenero(new GeneroDetalleDTO("Acción"));
        contenidoDTO.setTipo(new TipoDetalleDTO("Película"));
        contenidoDTO.setEstado(new EstadoDetalleDTO("Visto"));

        requestDTO = new RequestContenidoDTO(
                "https://imagen.com/superman.jpg",
                "Superman",
                5,
                "Película de superhéroes",
                "Acción",
                "Película",
                "Visto"
        );

        when(authentication.getName()).thenReturn("david");
    }

    @Test
    void crearDebeCrearContenidoParaUsuarioAutenticado() {
        when(contenidoService.crear("david", requestDTO)).thenReturn(contenidoDTO);

        ResponseEntity<ContenidoDTO> response = contenidoController.crear(authentication, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Superman", response.getBody().getTitulo());
        verify(contenidoService).crear("david", requestDTO);
    }

    @Test
    void obtenerPorIdDebeDevolverContenidoDelUsuarioAutenticado() {
        when(contenidoService.obtenerPorId("david", 1L)).thenReturn(contenidoDTO);

        ResponseEntity<ContenidoDTO> response = contenidoController.obtenerPorId(authentication, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(contenidoService).obtenerPorId("david", 1L);
    }

    @Test
    void listarPorUsuarioDebeDevolverContenidosDelUsuarioAutenticado() {
        when(contenidoService.listarPorUsuario("david")).thenReturn(List.of(contenidoDTO));

        ResponseEntity<List<ContenidoDTO>> response = contenidoController.listarPorUsuario(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(contenidoService).listarPorUsuario("david");
    }

    @Test
    void actualizarDebeActualizarContenidoDelUsuarioAutenticado() {
        when(contenidoService.actualizar("david", 1L, requestDTO)).thenReturn(contenidoDTO);

        ResponseEntity<ContenidoDTO> response = contenidoController.actualizar(authentication, 1L, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Superman", response.getBody().getTitulo());
        verify(contenidoService).actualizar("david", 1L, requestDTO);
    }

    @Test
    void eliminarDebeEliminarContenidoDelUsuarioAutenticado() {
        ResponseEntity<Response> response = contenidoController.eliminar(authentication, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Contenido eliminado correctamente", response.getBody().getMessage());
        verify(contenidoService).eliminar("david", 1L);
    }

    @Test
    void filtrarDebeDevolverContenidosFiltradosDelUsuarioAutenticado() {
        when(contenidoService.filtrar("david", "Película", "Acción", "Visto", 5, "Super"))
                .thenReturn(List.of(contenidoDTO));

        ResponseEntity<List<ContenidoDTO>> response = contenidoController.filtrar(
                authentication,
                "Película",
                "Acción",
                "Visto",
                5,
                "Super"
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(contenidoService).filtrar("david", "Película", "Acción", "Visto", 5, "Super");
    }
}
