package com.example.trackify.config.security;

import com.example.trackify.exceptions.ForbiddenException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDenied implements AccessDeniedHandler {

    private final Logger logger =
            LoggerFactory.getLogger(JwtAccessDenied.class);

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        logger.error("Error de autorización: {}",
                accessDeniedException.getMessage());

        throw new ForbiddenException(

                "No tienes permisos para acceder a este recurso"
        );
    }
}