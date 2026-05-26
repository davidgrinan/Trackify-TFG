package com.example.trackify.repository.Role;

import com.example.trackify.Enum.RoleType;
import com.example.trackify.entity.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRoleRepository extends CrudRepository<Role, Integer> {

    List<Role> findByIdIn(List<Integer> ids);

    boolean existsByRol(RoleType rol);
}
