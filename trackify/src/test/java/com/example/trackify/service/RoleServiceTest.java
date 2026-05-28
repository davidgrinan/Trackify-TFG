package com.example.trackify.service;

import com.example.trackify.Enum.RoleType;
import com.example.trackify.entity.Role;
import com.example.trackify.exceptions.NotFoundEntityException;
import com.example.trackify.repository.Role.IRoleRepository;
import com.example.trackify.service.role.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private IRoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void obtenerPorRolDebeDevolverRol() {
        Role role = new Role();
        role.setId(1);
        role.setRol(RoleType.USER);

        when(roleRepository.findByRol(RoleType.USER)).thenReturn(Optional.of(role));

        Role resultado = roleService.obtenerPorRol(RoleType.USER);

        assertEquals(RoleType.USER, resultado.getRol());
        verify(roleRepository).findByRol(RoleType.USER);
    }

    @Test
    void obtenerPorRolDebeLanzarNotFound() {
        when(roleRepository.findByRol(RoleType.ADMIN)).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> roleService.obtenerPorRol(RoleType.ADMIN));
    }
}
