package com.example.trackify.service.role;

import com.example.trackify.Enum.RoleType;
import com.example.trackify.entity.Role;

public interface IRoleService {

    Role obtenerPorRol(RoleType rol);
}