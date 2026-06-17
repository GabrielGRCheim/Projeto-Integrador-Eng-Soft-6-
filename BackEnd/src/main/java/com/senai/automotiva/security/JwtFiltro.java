package com.senai.automotiva.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que intercepta cada requisição HTTP, extrai o token JWT do cabeçalho
 * {@code Authorization: Bearer <token>} e autentica o usuário no contexto de segurança.
 *
 * <p>Executado uma única vez por requisição ({@link OncePerRequestFilter}).</p>
 */
@Component
public class JwtFiltro extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest requisicao,
                                    HttpServletResponse resposta,
                                    FilterChain cadeia) throws ServletException, IOException {

        String cabecalhoAuth = requisicao.getHeader("Authorization");

        // Ignora requisições sem Bearer token
        if (cabecalhoAuth == null || !cabecalhoAuth.startsWith("Bearer ")) {
            cadeia.doFilter(requisicao, resposta);
            return;
        }

        String token = cabecalhoAuth.substring(7);

        try {
            String email = jwtUtil.extrairEmail(token);

            // Só processa se houver e-mail e o contexto ainda não tiver autenticação
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = usuarioDetailsService.loadUserByUsername(email);

                if (jwtUtil.tokenValido(token, userDetails)) {
                    UsernamePasswordAuthenticationToken autenticacao =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(requisicao));
                    SecurityContextHolder.getContext().setAuthentication(autenticacao);
                }
            }
        } catch (Exception e) {
            // Token inválido ou expirado — deixa a requisição prosseguir sem autenticação
            // O Spring Security retornará 401 automaticamente para rotas protegidas
        }

        cadeia.doFilter(requisicao, resposta);
    }
}
