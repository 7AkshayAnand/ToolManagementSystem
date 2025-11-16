package com.toolmanagementsystem.demo.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.BeanProperty;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${api.server.url}")
    private String serverUrl;
    @Value("${api.server.description}")
    private String description;
    @Bean
    public OpenAPI myCustomConfig(){
        return new OpenAPI().info(
                new Info().title("Facility Capicity Planning System").description("Tool Entry Module")
        ).servers(List.of(new Server().url(serverUrl).description(description)));
    }
}
