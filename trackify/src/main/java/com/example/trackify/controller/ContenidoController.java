package com.example.trackify.controller;

import com.example.trackify.dto.Contenido.ContenidoDTO;
import com.example.trackify.dto.Contenido.RequestContenidoDTO;
import com.example.trackify.exceptions.Response;
import com.example.trackify.service.contenido.IContenidoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/contenido")
@AllArgsConstructor
public class ContenidoController {

    private final IContenidoService contenidoService;

    private final Logger logger =
            LoggerFactory.getLogger(ContenidoController.class);

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<ContenidoDTO> crear(@PathVariable Long usuarioId, @Valid @RequestBody RequestContenidoDTO dto) {

        logger.info("Petición para crear contenido del usuario {}", usuarioId);

        ContenidoDTO contenidoDTO =
                contenidoService.crear(usuarioId, dto);

        logger.info("Contenido creado correctamente");

        return new ResponseEntity<>(
                contenidoDTO,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContenidoDTO> obtenerPorId(@PathVariable Long id) {

        logger.info("Petición para obtener contenido con id {}", id);

        ContenidoDTO contenidoDTO =
                contenidoService.obtenerPorId(id);

        return ResponseEntity.ok(contenidoDTO);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ContenidoDTO>> listarPorUsuario(@PathVariable Long usuarioId) {

        logger.info("Petición para listar contenidos del usuario {}", usuarioId);

        List<ContenidoDTO> contenidos =
                contenidoService.listarPorUsuario(usuarioId);

        return ResponseEntity.ok(contenidos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContenidoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody RequestContenidoDTO dto) {

        logger.info("Petición para actualizar contenido {}", id);

        ContenidoDTO contenidoDTO =
                contenidoService.actualizar(id, dto);

        logger.info("Contenido actualizado correctamente");

        return ResponseEntity.ok(contenidoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> eliminar(@PathVariable Long id) {

        logger.info("Petición para eliminar contenido {}", id);

        contenidoService.eliminar(id);

        logger.info("Contenido eliminado correctamente");

        return ResponseEntity.ok(
                Response.ok("Contenido eliminado correctamente")
        );
    }

    @GetMapping("/filtros/{usuarioId}")
    public ResponseEntity<List<ContenidoDTO>> filtrar(@PathVariable Long usuarioId,
                                                      @RequestParam(required = false) String tipo,
                                                      @RequestParam(required = false) String genero,
                                                      @RequestParam(required = false) String estado,
                                                      @RequestParam(required = false) Integer valoracion,
                                                      @RequestParam(required = false) String titulo)
    {
        logger.info("Petición de filtrado para usuario {}", usuarioId);

        List<ContenidoDTO> contenidos =
                contenidoService.filtrar(
                        usuarioId,
                        tipo,
                        genero,
                        estado,
                        valoracion,
                        titulo
                );

        return ResponseEntity.ok(contenidos);
    }
}