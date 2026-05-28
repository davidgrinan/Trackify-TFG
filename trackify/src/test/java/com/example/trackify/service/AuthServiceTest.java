package com.example.trackify.service;

import com.example.trackify.Enum.RoleType;
import com.example.trackify.config.security.JwtTokenProvider;
import com.example.trackify.dto.Usuario.CrearUsuarioDTO;
import com.example.trackify.dto.Usuario.LoginUsuarioDTO;
import com.example.trackify.entity.Role;
import com.example.trackify.entity.Usuario;
import com.example.trackify.exceptions.DuplicateEntityException;
import com.example.trackify.exceptions.UnauthorizedException;
import com.example.trackify.mapper.UsuarioMapper;
import com.example.trackify.repository.Usuario.IUsuarioRepository;
import com.example.trackify.service.auth.AuthService;
import com.example.trackify.service.role.IRoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private IRoleService roleService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerDebeGuardarUsuarioConRolUser() {
        CrearUsuarioDTO dto = new CrearUsuarioDTO("david", "David123", "david@gmail.com");
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("david");
        usuario.setPassword("David123");
        usuario.setEmail("david@gmail.com");

        Role roleUser = new Role();
        roleUser.setRol(RoleType.USER);

        when(usuarioRepository.existsByUsername("david")).thenReturn(false);
        when(usuarioRepository.existsByEmail("david@gmail.com")).thenReturn(false);
        when(usuarioMapper.toUsuarioFromCreateUsuarioDTO(dto)).thenReturn(usuario);
        when(passwordEncoder.encode("David123")).thenReturn("password-encriptada");
        when(roleService.obtenerPorRol(RoleType.USER)).thenReturn(roleUser);

        authService.register(dto);

        assertEquals("password-encriptada", usuario.getPassword());
        assertNotNull(usuario.getFechaCreacion());
        assertTrue(usuario.getRoles().contains(roleUser));
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void registerDebeLanzarDuplicateSiUsernameExiste() {
        CrearUsuarioDTO dto = new CrearUsuarioDTO("david", "David123", "david@gmail.com");
        when(usuarioRepository.existsByUsername("david")).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> authService.register(dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void loginDebeDevolverToken() {
        LoginUsuarioDTO dto = new LoginUsuarioDTO("david", "David123");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("token-jwt");

        String token = authService.login(dto);

        assertEquals("token-jwt", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(authentication);
    }

    @Test
    void loginDebeLanzarUnauthorizedSiCredencialesInvalidas() {
        LoginUsuarioDTO dto = new LoginUsuarioDTO("david", "mal");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("credenciales inválidas"));

        assertThrows(UnauthorizedException.class, () -> authService.login(dto));
    }
}
