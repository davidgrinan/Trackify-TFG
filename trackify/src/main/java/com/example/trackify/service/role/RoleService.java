package com.example.trackify.service.role;

import com.example.trackify.entity.Role;
import com.example.trackify.repository.Role.IRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {

    private final IRoleRepository roleRepository;

    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> obtenerRolesByNombre(List<Integer> nombre) {
        return roleRepository.findByIdIn(nombre);
    }
}
