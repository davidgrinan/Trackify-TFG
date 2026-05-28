package com.example.trackify.repository.Role;

import com.example.trackify.Enum.RoleType;
import com.example.trackify.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByRol(RoleType rol);

    boolean existsByRol(RoleType rol);
}