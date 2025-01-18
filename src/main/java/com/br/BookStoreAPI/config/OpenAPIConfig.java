package com.br.BookStoreAPI.config;

import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    private final String devUrl = "http://localhost:8080";

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");


        Contact contact = new Contact();
        contact.setEmail("luann.deSousa@jala.university");
        contact.setName("capstone");
        contact.setUrl("");

        Info info = new Info()
                .title("Final project API Documentation")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints for my reddit simulation API");

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
