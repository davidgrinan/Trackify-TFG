package com.example.trackify.repository.Estado;

import com.example.trackify.entity.Estado;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IEstadoRepository extends CrudRepository<Estado, Long> {
    boolean existsByNombre(String nombre);

    Optional<Estado> findByNombre(String nombre);
}
