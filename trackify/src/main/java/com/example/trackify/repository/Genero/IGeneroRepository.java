package com.example.trackify.repository.Genero;

import com.example.trackify.entity.Genero;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IGeneroRepository extends CrudRepository<Genero, Long> {
    boolean existsByNombre(String nombre);

    Optional<Genero> findByNombre(String nombre);
}
