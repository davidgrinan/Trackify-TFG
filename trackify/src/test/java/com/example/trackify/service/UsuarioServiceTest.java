package com.example.trackify.service;

import com.example.trackify.Enum.RoleType;
import com.example.trackify.entity.Role;
import com.example.trackify.entity.Usuario;
import com.example.trackify.exceptions.NotFoundEntityException;
import com.example.trackify.repository.Usuario.IUsuarioRepository;
import com.example.trackify.service.usuario.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private IUsuarioRepository userRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void loadUserByUsernameDebeDevolverUserDetails() {
        Role role = new Role();
        role.setRol(RoleType.USER);

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("david");
        usuario.setPassword("password");
        usuario.setRoles(Set.of(role));

        when(userRepository.findByUsername("david")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = usuarioService.loadUserByUsername("david");

        assertEquals("david", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));
        verify(userRepository).findByUsername("david");
    }

    @Test
    void loadUserByUsernameDebeLanzarNotFound() {
        when(userRepository.findByUsername("inexistente")).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> usuarioService.loadUserByUsername("inexistente"));
    }
}
