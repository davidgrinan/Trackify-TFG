package com.example.trackify.controller;

import com.example.trackify.dto.Tipo.TipoDetalleDTO;
import com.example.trackify.exceptions.Response;
import com.example.trackify.service.tipo.ITipoService;
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
@Tag(name = "Tipos", description = "Consulta de tipos de contenido")
public class TipoController {

    private final ITipoService tipoService;
    private final Logger logger = LoggerFactory.getLogger(TipoController.class);

    @Operation(summary = "Listar tipos", description = "Obtiene todos los tipos disponibles")
    @GetMapping("/tipo")
    public ResponseEntity<List<TipoDetalleDTO>> listar() {
        logger.info("listando tipos");
        return ResponseEntity.ok(tipoService.listarTodos());
    }

    @Operation(summary = "Obtener tipo por id", description = "Obtiene un tipo concreto por su id")
    @GetMapping("/tipo/{id}")
    public ResponseEntity<TipoDetalleDTO> obtener(@PathVariable Long id) {
        logger.info("obteniendo tipo con id {}", id);
        return ResponseEntity.ok(tipoService.obtenerPorId(id));
    }
}