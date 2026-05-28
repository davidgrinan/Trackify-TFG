package com.example.trackify.service.role;

import com.example.trackify.Enum.RoleType;
import com.example.trackify.entity.Role;
import com.example.trackify.exceptions.NotFoundEntityException;
import com.example.trackify.repository.Role.IRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService implements IRoleService {

    private final IRoleRepository roleRepository;

    @Override
    public Role obtenerPorRol(RoleType rol) {
        return roleRepository.findByRol(rol)
                .orElseThrow(() ->
                        new NotFoundEntityException("Rol " + rol + " no encontrado")
                );
    }
}