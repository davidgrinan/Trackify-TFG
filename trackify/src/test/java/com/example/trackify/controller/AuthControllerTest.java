package com.example.trackify.controller;

import com.example.trackify.dto.Usuario.CrearUsuarioDTO;
import com.example.trackify.dto.Usuario.LoginUsuarioDTO;
import com.example.trackify.exceptions.Response;
import com.example.trackify.service.auth.IAuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private IAuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void registerDebeCrearUsuario() {
        CrearUsuarioDTO dto = new CrearUsuarioDTO("david", "David123", "david@gmail.com");

        ResponseEntity<Response> response = authController.register(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Usuario registrado correctamente", response.getBody().getMessage());
        verify(authService).register(dto);
    }

    @Test
    void loginDebeDevolverToken() {
        LoginUsuarioDTO dto = new LoginUsuarioDTO("david", "David123");
        when(authService.login(dto)).thenReturn("token-jwt");

        ResponseEntity<Response> response = authController.login(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("token-jwt", response.getBody().getMessage());
        verify(authService).login(dto);
    }
}
