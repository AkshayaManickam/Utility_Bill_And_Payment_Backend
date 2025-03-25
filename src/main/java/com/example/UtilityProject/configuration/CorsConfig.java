package com.example.UtilityProject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // CORS Configuration
        CorsConfiguration corsConfig = new CorsConfiguration();

        // Allow specific origin
        corsConfig.setAllowedOrigins(List.of("http://localhost:4200"));  // Your Angular front-end URL

        // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow any headers
        corsConfig.setAllowedHeaders(List.of("*"));

        // Allow credentials (cookies, authentication headers, etc.)
        corsConfig.setAllowCredentials(true);

        // Map the CORS configuration to all URL paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Apply to all endpoints

        // Return the CORS filter
        return new CorsFilter(source);
    }
}
