package com.iplens.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("IP Lens API")
                        .version("1.0.0")
                        .description("API REST para búsqueda y gestión de información de direcciones IP")
                        .contact(new Contact()
                                .name("IP Lens REST API Author: María Berenice Lozano Mercado")
                                .email("bere.lozano.m@gmail.com")));
    }
}
