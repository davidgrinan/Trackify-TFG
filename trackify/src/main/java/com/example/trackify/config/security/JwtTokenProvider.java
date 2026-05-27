package com.example.trackify.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Service
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret_key;

    @Value("${jwt.expiration}")
    private int expiration;

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    public String generateToken(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();

        log.info("Generando token para usuario: {}", user.getUsername());

        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                .signWith(getKey())
                .compact();
    }

    public String extractUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsernameFromToken(token);

        boolean valid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);

        if (!valid) {
            log.warn("Token inválido para usuario: {}", username);
        }

        return valid;
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public <T> T getClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    private Date extractExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("Error leyendo token JWT", e);
            throw new RuntimeException("Token inválido o corrupto", e);
        }
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}