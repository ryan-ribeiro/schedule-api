package com.example.scheduleapi.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("scheduleapi - API")
                        .description("API de um agendador de tarefas")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Ryan Ribeiro")
                                .email("RyanRodrigues0071234@gmail.com")
                                .url("")
                        )
                );
    }
}