package com.example.trackify.controller;

import com.example.trackify.dto.Tipo.*;
import com.example.trackify.service.tipo.ITipoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TipoController {

    private final ITipoService tipoService;
    private final Logger logger =
            LoggerFactory.getLogger(TipoController.class);

    @GetMapping("/tipo")
    public ResponseEntity<List<TipoDetalleDTO>> listar() {
        logger.info("listando tipos");
        return ResponseEntity.ok(tipoService.listarTodos());
    }

    @GetMapping("/tipo/{id}")
    public ResponseEntity<TipoDetalleDTO> obtener(@PathVariable Long id) {
        logger.info("obteniendo tipo con id {}", id);
        return ResponseEntity.ok(tipoService.obtenerPorId(id));
    }
}
