package com.example.trackify.config.security;

import com.example.trackify.exceptions.UnauthorizedException;
import com.example.trackify.service.usuario.IUsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtTokenProvider jwtService;
    private final IUsuarioService userService;

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = getTokenFromRequest(request);

            if (token == null) {
                logger.debug("No JWT token found in request");
                filterChain.doFilter(request, response);
                return;
            }

            logger.debug("JWT token recibido");

            String username = jwtService.extractUsernameFromToken(token);

            if (username == null) {
                throw new UnauthorizedException("Token inválido: no se pudo extraer usuario");
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails user = userService.loadUserByUsername(username);

                if (!jwtService.isTokenValid(token, user)) {
                    logger.warn("Token inválido para usuario {}", username);
                    throw new UnauthorizedException("Token inválido o expirado");
                }

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);

                logger.info("Usuario autenticado correctamente: {}", username);
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            logger.error("Error en JwtAuthenticationFilter", ex);
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {

        String authHeader = request.getHeader(JwtTokenProvider.TOKEN_HEADER);

        if (StringUtils.hasText(authHeader)
                && authHeader.startsWith(JwtTokenProvider.TOKEN_PREFIX)) {

            return authHeader.substring(JwtTokenProvider.TOKEN_PREFIX.length());
        }

        return null;
    }
}