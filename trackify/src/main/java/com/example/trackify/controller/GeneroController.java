package com.example.trackify.controller;

import com.example.trackify.dto.Genero.GeneroDetalleDTO;
import com.example.trackify.service.genero.IGeneroService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class GeneroController {

    private final IGeneroService generoService;

    @GetMapping("/genero")
    public ResponseEntity<List<GeneroDetalleDTO>> listar() {
        return ResponseEntity.ok(generoService.listarTodos());
    }

    @GetMapping("/genero/{id}")
    public ResponseEntity<GeneroDetalleDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(generoService.obtenerPorId(id));
    }
}
