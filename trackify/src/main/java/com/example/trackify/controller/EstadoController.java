package com.example.trackify.controller;

import com.example.trackify.dto.Estado.EstadoDetalleDTO;
import com.example.trackify.service.estado.EstadoService;
import com.example.trackify.service.estado.IEstadoService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/estados")
@AllArgsConstructor
public class EstadoController {

    private final IEstadoService estadoService;
    private final Logger logger = LoggerFactory.getLogger(EstadoController.class);

    @GetMapping("/estados")
    public ResponseEntity<List<EstadoDetalleDTO>> listar() {
        logger.info("listando estados");
        return ResponseEntity.ok(estadoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoDetalleDTO> obtener(@PathVariable Long id) {
        logger.info("obteniendo estado con id {}", id);
        return ResponseEntity.ok(estadoService.obtenerPorId(id));
    }
}
