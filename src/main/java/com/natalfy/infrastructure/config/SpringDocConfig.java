package com.natalfy.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Define que a API exige um botão de Autorização chamado "bearer-key"
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                // Aplica essa exigência para todas as rotas (coloca o cadeado em tudo)
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
                // Informações bonitinhas no topo do Swagger
                .info(new Info()
                        .title("Natalfy API")
                        .description("API Restful da Clínica Obstétrica Natalfy - Gestão de Gestantes, Médicos e Consultas.")
                        .contact(new Contact()
                                .name("Time de Engenharia Natalfy")
                                .email("suporte@natalfy.com"))
                        .version("1.0.0"));
    }
}