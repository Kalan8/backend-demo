package com.example.hibernatedemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS configuration for the application.
 * <p>
 * This configuration class defines how the backend API handles cross-origin requests
 * (CORS). It enables the frontend (running on a different origin, such as
 * {@code http://localhost:5173}) to communicate with the backend.
 * </p>
 */
@Configuration
public class CorsConfig {

    @Value("${frontend.url}")
    private String[] frontendUrl;

/**
 * Defines the global CORS configuration for the application.
 * <p>
 * This method registers a {@link WebMvcConfigurer} bean that customizes the handling of
 * Cross-Origin Resource Sharing (CORS) requests across all REST endpoints in the application.
 * It allows the frontend (typically running on a different origin, such as
 * {@code http://localhost:5173}) to communicate with the backend.
 * </p>
 */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(frontendUrl)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
            }
        };
    }
}
