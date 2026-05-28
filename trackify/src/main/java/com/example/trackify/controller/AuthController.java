package com.example.trackify.controller;

import com.example.trackify.dto.Usuario.CrearUsuarioDTO;
import com.example.trackify.dto.Usuario.LoginUsuarioDTO;
import com.example.trackify.exceptions.CreateEntityException;
import com.example.trackify.exceptions.DuplicateEntityException;
import com.example.trackify.exceptions.Response;
import com.example.trackify.exceptions.UnauthorizedException;
import com.example.trackify.service.auth.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Registro y login de usuarios")
public class AuthController {

    private final IAuthService authService;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario con rol USER por defecto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "409", description = "Usuario o email duplicado",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody CrearUsuarioDTO dto) {

        logger.info("PETICIÓN REGISTER usuario {}", dto.getNombreUsuario());

        try {
            authService.register(dto);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Response.ok("Usuario registrado correctamente"));

        } catch (DuplicateEntityException | CreateEntityException ex) {
            throw ex;

        } catch (Exception ex) {
            throw new CreateEntityException("Error registrando usuario", dto, ex);
        }
    }

    @Operation(summary = "Login de usuario", description = "Autentica un usuario y devuelve un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login correcto"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<Response> login(@Valid @RequestBody LoginUsuarioDTO dto) {

        logger.info("PETICIÓN LOGIN usuario {}", dto.getNombreUsuario());

        try {
            String token = authService.login(dto);
            return ResponseEntity.ok(Response.ok(token));

        } catch (UnauthorizedException ex) {
            throw ex;

        } catch (Exception ex) {
            throw new UnauthorizedException("Error en autenticación");
        }
    }
}