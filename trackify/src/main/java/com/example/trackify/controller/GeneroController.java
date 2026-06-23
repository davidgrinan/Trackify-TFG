package com.example.trackify.controller;

import com.example.trackify.dto.Genero.GeneroDetalleDTO;
import com.example.trackify.service.genero.IGeneroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Géneros", description = "Consulta de géneros")
public class GeneroController {

    private final IGeneroService generoService;
    private final Logger logger = LoggerFactory.getLogger(GeneroController.class);

    @Operation(summary = "Listar géneros", description = "Obtiene todos los géneros disponibles")
    @GetMapping("/genero")
    public ResponseEntity<List<GeneroDetalleDTO>> listar() {
        logger.info("listando géneros");
        return ResponseEntity.ok(generoService.listarTodos());
    }

    @Operation(summary = "Obtener género por id", description = "Obtiene un género concreto por su id")
    @GetMapping("/genero/{id}")
    public ResponseEntity<GeneroDetalleDTO> obtener(@PathVariable Long id) {
        logger.info("obteniendo género con id {}", id);
        return ResponseEntity.ok(generoService.obtenerPorId(id));
    }
}