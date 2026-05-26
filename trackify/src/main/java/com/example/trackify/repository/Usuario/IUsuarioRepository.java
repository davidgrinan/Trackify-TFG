package com.example.trackify.repository.Usuario;

import com.example.trackify.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IUsuarioRepository extends CrudRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}
