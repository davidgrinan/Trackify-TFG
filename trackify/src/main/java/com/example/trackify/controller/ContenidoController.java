package com.example.trackify.controller;

import com.example.trackify.dto.Contenido.ContenidoDTO;
import com.example.trackify.dto.Contenido.RequestContenidoDTO;
import com.example.trackify.exceptions.Response;
import com.example.trackify.service.contenido.IContenidoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/contenido")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ContenidoController {

    private final IContenidoService contenidoService;

    private final Logger logger =
            LoggerFactory.getLogger(ContenidoController.class);

    @PostMapping
    public ResponseEntity<ContenidoDTO> crear(Authentication authentication,
                                              @Valid @RequestBody RequestContenidoDTO dto) {

        String username = authentication.getName();

        logger.info("Petición para crear contenido del usuario {}", username);

        ContenidoDTO contenidoDTO = contenidoService.crear(username, dto);

        return ResponseEntity.ok(contenidoDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContenidoDTO> obtenerPorId(Authentication authentication,
                                                     @PathVariable Long id) {

        String username = authentication.getName();

        logger.info("Petición para obtener contenido {} del usuario {}", id, username);

        ContenidoDTO contenidoDTO = contenidoService.obtenerPorId(username, id);

        return ResponseEntity.ok(contenidoDTO);
    }

    @GetMapping("/listarporusuario")
    public ResponseEntity<List<ContenidoDTO>> listarPorUsuario(Authentication authentication) {

        String username = authentication.getName();

        logger.info("Petición para listar contenidos del usuario {}", username);

        List<ContenidoDTO> contenidos = contenidoService.listarPorUsuario(username);

        return ResponseEntity.ok(contenidos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContenidoDTO> actualizar(Authentication authentication,
                                                   @PathVariable Long id,
                                                   @Valid @RequestBody RequestContenidoDTO dto) {

        String username = authentication.getName();

        logger.info("Petición para actualizar contenido {} del usuario {}", id, username);

        ContenidoDTO contenidoDTO = contenidoService.actualizar(username, id, dto);

        return ResponseEntity.ok(contenidoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> eliminar(Authentication authentication,
                                             @PathVariable Long id) {

        String username = authentication.getName();

        logger.info("Petición para eliminar contenido {} del usuario {}", id, username);

        contenidoService.eliminar(username, id);

        return ResponseEntity.ok(
                Response.ok("Contenido eliminado correctamente")
        );
    }

    @GetMapping("/filtros")
    public ResponseEntity<List<ContenidoDTO>> filtrar(Authentication authentication,
                                                      @RequestParam(required = false) String tipo,
                                                      @RequestParam(required = false) String genero,
                                                      @RequestParam(required = false) String estado,
                                                      @RequestParam(required = false) Integer valoracion,
                                                      @RequestParam(required = false) String titulo) {

        String username = authentication.getName();

        logger.info("Petición de filtrado para usuario {}", username);

        List<ContenidoDTO> contenidos = contenidoService.filtrar(
                username,
                tipo,
                genero,
                estado,
                valoracion,
                titulo
        );

        return ResponseEntity.ok(contenidos);
    }
}