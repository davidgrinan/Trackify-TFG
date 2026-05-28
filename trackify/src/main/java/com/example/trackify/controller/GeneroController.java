package com.example.trackify.controller;

import com.example.trackify.dto.Genero.GeneroDetalleDTO;
import com.example.trackify.exceptions.Response;
import com.example.trackify.service.genero.IGeneroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
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

    @Operation(summary = "Listar géneros", description = "Obtiene todos los géneros disponibles")
    @GetMapping("/genero")
    public ResponseEntity<List<GeneroDetalleDTO>> listar() {
        return ResponseEntity.ok(generoService.listarTodos());
    }

    @Operation(summary = "Obtener género por id", description = "Obtiene un género concreto por su id")
    @GetMapping("/genero/{id}")
    public ResponseEntity<GeneroDetalleDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(generoService.obtenerPorId(id));
    }
}