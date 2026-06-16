package com.senai.automotiva.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilita a proteção CSRF, essencial para o console do H2 conseguir fazer requisições
                .csrf(csrf -> csrf.disable())

                // 2. Define quais rotas são públicas (não exigem login ou tokens)
                .authorizeHttpRequests(auth -> auth
                        // Rotas do Swagger UI e da documentação JSON (v3)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Rota do console do Banco de Dados H2
                        .requestMatchers("/h2-console/**").permitAll()
                        // Qualquer outra rota da sua API exigirá autenticação
                        .anyRequest().permitAll()
                )

                // 3. Permite o uso de <frame> ou <iframe> apenas da mesma origem (SameOrigin).
                // O Console do H2 usa frames para renderizar a barra lateral e a tela de query.
                // Sem isso, o navegador bloqueia o H2 exibindo uma tela branca.
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                );

        return http.build();
    }
}
