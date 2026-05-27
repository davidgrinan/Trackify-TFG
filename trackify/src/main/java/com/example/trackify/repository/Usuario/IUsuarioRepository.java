package com.example.trackify.repository.Usuario;

import com.example.trackify.entity.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuarioRepository extends CrudRepository<Usuario, Long>
{
    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}

