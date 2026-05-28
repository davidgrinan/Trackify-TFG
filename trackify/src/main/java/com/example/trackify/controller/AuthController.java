package com.example.trackify.controller;

import com.example.trackify.dto.Usuario.CrearUsuarioDTO;
import com.example.trackify.dto.Usuario.LoginUsuarioDTO;
import com.example.trackify.exceptions.CreateEntityException;
import com.example.trackify.exceptions.DuplicateEntityException;
import com.example.trackify.exceptions.Response;
import com.example.trackify.exceptions.UnauthorizedException;
import com.example.trackify.service.auth.IAuthService;
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
public class AuthController {

    private final IAuthService authService;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody CrearUsuarioDTO dto) {

        logger.info("PETICIÓN REGISTER usuario {}", dto.getNombreUsuario());

        try {
            authService.register(dto);

            logger.info("REGISTER OK usuario {}", dto.getNombreUsuario());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Response.ok("Usuario registrado correctamente"));

        } catch (DuplicateEntityException ex) {

            logger.warn("REGISTER DUPLICADO usuario {} -> {}", dto.getNombreUsuario(), ex.getMessage());
            throw ex;

        } catch (CreateEntityException ex) {

            logger.error("ERROR CREATE REGISTER usuario {}", dto.getNombreUsuario(), ex);
            throw ex;

        } catch (Exception ex) {

            logger.error("ERROR INESPERADO REGISTER usuario {}", dto.getNombreUsuario(), ex);

            throw new CreateEntityException(
                    "Error registrando usuario",
                    dto,
                    ex
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@Valid @RequestBody LoginUsuarioDTO dto) {

        logger.info("PETICIÓN LOGIN usuario {}", dto.getNombreUsuario());

        try {
            String token = authService.login(dto);

            logger.info("LOGIN OK usuario {}", dto.getNombreUsuario());

            return ResponseEntity.ok(
                    Response.ok(token)
            );

        } catch (UnauthorizedException ex) {

            logger.warn("LOGIN FALLIDO usuario {} -> {}", dto.getNombreUsuario(), ex.getMessage());
            throw ex;

        } catch (Exception ex) {

            logger.error("ERROR INESPERADO LOGIN usuario {}", dto.getNombreUsuario(), ex);

            throw new UnauthorizedException("Error en autenticación");
        }
    }
}