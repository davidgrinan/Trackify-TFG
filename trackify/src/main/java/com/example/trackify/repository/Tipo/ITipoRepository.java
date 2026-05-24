package com.example.trackify.repository.Tipo;

import com.example.trackify.entity.Tipo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ITipoRepository extends CrudRepository<Tipo, Long> {
    boolean existsByNombre(String nombre);

    Optional<Tipo> findByNombre(String nombre);
}
