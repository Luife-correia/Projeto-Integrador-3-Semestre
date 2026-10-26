package com.gamebox.app.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {

                // Permite que o Angular (porta 4200) acesse o back-end
                registry.addMapping("/**")

                        // Define a origem permitida: Angular
                        .allowedOrigins("http://localhost:4200")

                        // Permite requisições GET, POST, PUT, DELETE e outras
                        .allowedMethods("*")

                        // Permite os cabeçalhos enviados nas requisições
                        .allowedHeaders("*");
            }
        };
    }
}