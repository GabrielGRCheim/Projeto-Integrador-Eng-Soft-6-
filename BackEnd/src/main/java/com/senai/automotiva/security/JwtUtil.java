package com.senai.automotiva.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Utilitário responsável por gerar, validar e extrair informações de tokens JWT.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String segredo;

    @Value("${jwt.expiracao-ms}")
    private long expiracaoMs;

    private SecretKey getChave() {
        return Keys.hmacShaKeyFor(segredo.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Gera um token JWT para o usuário autenticado.
     * O subject é o e-mail do usuário; o perfil é embutido como claim extra.
     */
    public String gerarToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("perfil", userDetails.getAuthorities().iterator().next().getAuthority())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiracaoMs))
                .signWith(getChave())
                .compact();
    }

    /** Extrai o e-mail (subject) do token. */
    public String extrairEmail(String token) {
        return parsearClaims(token).getSubject();
    }

    /** Verifica se o token é válido para o usuário informado. */
    public boolean tokenValido(String token, UserDetails userDetails) {
        String email = extrairEmail(token);
        return email.equals(userDetails.getUsername()) && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {
        return parsearClaims(token).getExpiration().before(new Date());
    }

    private Claims parsearClaims(String token) {
        return Jwts.parser()
                .verifyWith(getChave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
