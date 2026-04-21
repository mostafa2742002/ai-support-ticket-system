package com.mostafa.aisupport.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI Support Ticket System API")
                        .description("""
                                REST API for an AI-powered support ticket system built with Spring Boot, Spring AI, PostgreSQL, and JWT security.

                                Main capabilities:
                                - Public ticket creation
                                - Automatic AI triage
                                - Team and agent assignment
                                - Ticket comments/history
                                - Protected internal support operations
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Mostafa Mahmoud")
                                .email("mostafa19500mahmoud@gmail.com")
                                .url("https://github.com/mostafa2742002"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}