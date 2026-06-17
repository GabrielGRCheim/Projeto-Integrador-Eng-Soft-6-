package com.senai.automotiva.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Manutenção Automotiva — API")
                        .description("""
                                API REST para gerenciamento de clientes, veículos, ordens de serviço e estoque.
                                
                                **Como autenticar:**
                                1. Faça POST em `/api/auth/login` com e-mail e senha.
                                2. Copie o campo `token` da resposta.
                                3. Clique em **Authorize** (cadeado) e cole: `Bearer {token}`.
                                """)
                        .version("2.0.0")
                        .contact(new Contact().name("SENAI").email("contato@senai.br")))
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME, new SecurityScheme()
                                .name(SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token JWT obtido em POST /api/auth/login")));
    }
}
