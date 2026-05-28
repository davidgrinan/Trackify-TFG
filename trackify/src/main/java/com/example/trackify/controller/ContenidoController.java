package com.example.trackify.controller;

import com.example.trackify.dto.Contenido.ContenidoDTO;
import com.example.trackify.dto.Contenido.RequestContenidoDTO;
import com.example.trackify.exceptions.Response;
import com.example.trackify.service.contenido.IContenidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Contenido", description = "Gestión de contenidos multimedia del usuario autenticado")
public class ContenidoController {

    private final IContenidoService contenidoService;
    private final Logger logger = LoggerFactory.getLogger(ContenidoController.class);

    @Operation(summary = "Crear contenido", description = "Crea un contenido asociado al usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido creado correctamente",
                    content = @Content(schema = @Schema(implementation = ContenidoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "403", description = "Sin permisos",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping
    public ResponseEntity<ContenidoDTO> crear(Authentication authentication,
                                              @Valid @RequestBody RequestContenidoDTO dto) {

        String username = authentication.getName();
        logger.info("Petición para crear contenido del usuario {}", username);

        return ResponseEntity.ok(contenidoService.crear(username, dto));
    }

    @Operation(summary = "Obtener contenido por id", description = "Obtiene un contenido del usuario autenticado por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido encontrado",
                    content = @Content(schema = @Schema(implementation = ContenidoDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Contenido no encontrado",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContenidoDTO> obtenerPorId(Authentication authentication,
                                                     @PathVariable Long id) {

        String username = authentication.getName();
        logger.info("Petición para obtener contenido {} del usuario {}", id, username);

        return ResponseEntity.ok(contenidoService.obtenerPorId(username, id));
    }

    @Operation(summary = "Listar contenidos del usuario", description = "Lista todos los contenidos del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de contenidos"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/listarporusuario")
    public ResponseEntity<List<ContenidoDTO>> listarPorUsuario(Authentication authentication) {

        String username = authentication.getName();
        logger.info("Petición para listar contenidos del usuario {}", username);

        return ResponseEntity.ok(contenidoService.listarPorUsuario(username));
    }

    @Operation(summary = "Actualizar contenido", description = "Actualiza un contenido del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido actualizado",
                    content = @Content(schema = @Schema(implementation = ContenidoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Contenido no encontrado",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ContenidoDTO> actualizar(Authentication authentication,
                                                   @PathVariable Long id,
                                                   @Valid @RequestBody RequestContenidoDTO dto) {

        String username = authentication.getName();
        logger.info("Petición para actualizar contenido {} del usuario {}", id, username);

        return ResponseEntity.ok(contenidoService.actualizar(username, id, dto));
    }

    @Operation(summary = "Eliminar contenido", description = "Elimina un contenido del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido eliminado",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Contenido no encontrado",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> eliminar(Authentication authentication,
                                             @PathVariable Long id) {

        String username = authentication.getName();
        logger.info("Petición para eliminar contenido {} del usuario {}", id, username);

        contenidoService.eliminar(username, id);

        return ResponseEntity.ok(Response.ok("Contenido eliminado correctamente"));
    }

    @Operation(summary = "Filtrar contenidos", description = "Filtra los contenidos del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado filtrado de contenidos"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/filtros")
    public ResponseEntity<List<ContenidoDTO>> filtrar(Authentication authentication,
                                                      @RequestParam(required = false) String tipo,
                                                      @RequestParam(required = false) String genero,
                                                      @RequestParam(required = false) String estado,
                                                      @RequestParam(required = false) Integer valoracion,
                                                      @RequestParam(required = false) String titulo) {

        String username = authentication.getName();
        logger.info("Petición de filtrado para usuario {}", username);

        return ResponseEntity.ok(
                contenidoService.filtrar(username, tipo, genero, estado, valoracion, titulo)
        );
    }
}