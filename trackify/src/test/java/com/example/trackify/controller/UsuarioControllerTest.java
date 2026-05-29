package com.example.trackify.controller;

import com.example.trackify.dto.Usuario.CambiarPasswordDTO;
import com.example.trackify.exceptions.Response;
import com.example.trackify.service.usuario.IUsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private IUsuarioService usuarioService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UsuarioController usuarioController;

    private CambiarPasswordDTO dto;

    @BeforeEach
    void setUp() {
        dto = new CambiarPasswordDTO();
        dto.setNuevaPassword("NuevaPassword123");

        when(authentication.getName()).thenReturn("david");
    }

    @Test
    void cambiarPasswordDebeActualizarPasswordDelUsuarioAutenticado() {
        ResponseEntity<Response> response =
                usuarioController.cambiarPassword(authentication, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Contraseña actualizada correctamente", response.getBody().getMessage());

        verify(usuarioService).cambiarPassword("david", "NuevaPassword123");
    }
}