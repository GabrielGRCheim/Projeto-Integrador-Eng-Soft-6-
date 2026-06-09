package com.senai.automotiva.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Manutenção Automotiva - API")
                        .description("API REST para gerenciamento de clientes, veículos, ordens de serviço e estoque de uma oficina mecânica.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SENAI")
                                .email("contato@senai.br")));
    }
}
