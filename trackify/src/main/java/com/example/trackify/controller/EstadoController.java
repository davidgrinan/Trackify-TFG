package com.example.trackify.controller;

import com.example.trackify.dto.Estado.EstadoDetalleDTO;
import com.example.trackify.exceptions.Response;
import com.example.trackify.service.estado.IEstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Estados", description = "Consulta de estados de contenido")
public class EstadoController {

    private final IEstadoService estadoService;
    private final Logger logger = LoggerFactory.getLogger(EstadoController.class);

    @Operation(summary = "Listar estados", description = "Obtiene todos los estados disponibles")
    @GetMapping("/estado")
    public ResponseEntity<List<EstadoDetalleDTO>> listar() {
        logger.info("listando estados");
        return ResponseEntity.ok(estadoService.listarTodos());
    }

    @Operation(summary = "Obtener estado por id", description = "Obtiene un estado concreto por su id")
    @GetMapping("/estado/{id}")
    public ResponseEntity<EstadoDetalleDTO> obtener(@PathVariable Long id) {
        logger.info("obteniendo estado con id {}", id);
        return ResponseEntity.ok(estadoService.obtenerPorId(id));
    }
}