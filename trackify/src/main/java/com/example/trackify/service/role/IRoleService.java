package com.example.trackify.service.role;

import com.example.trackify.entity.Role;

import java.util.List;

public interface IRoleService {

    List<Role> obtenerRolesByNombre(List<Integer> nombre);
}
