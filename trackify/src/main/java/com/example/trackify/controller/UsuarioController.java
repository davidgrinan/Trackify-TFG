package com.example.trackify.controller;

import com.example.trackify.dto.Usuario.CambiarPasswordDTO;
import com.example.trackify.exceptions.Response;
import com.example.trackify.service.usuario.IUsuarioService;
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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/usuario")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuario", description = "Gestión del usuario autenticado")
public class UsuarioController {

    private final IUsuarioService usuarioService;
    private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Operation(
            summary = "Cambiar contraseña",
            description = "Permite al usuario autenticado cambiar su contraseña"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Contraseña actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuario no autenticado",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuario sin permisos",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = Response.class))
            )
    })
    @PutMapping("/password")
    public ResponseEntity<Response> cambiarPassword(Authentication authentication,
                                                    @Valid @RequestBody CambiarPasswordDTO dto) {

        String username = authentication.getName();

        logger.info("Petición para cambiar contraseña del usuario {}", username);

        usuarioService.cambiarPassword(
                username,
                dto.getNuevaPassword()
        );

        logger.info("Contraseña actualizada correctamente para el usuario {}", username);

        return ResponseEntity.ok(
                Response.ok("Contraseña actualizada correctamente")
        );
    }
}