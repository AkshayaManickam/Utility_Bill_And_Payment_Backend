package com.example.UtilityProject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/***")  // Allow all endpoints
                .allowedOrigins("http://localhost:4200")  // Allow requests from Angular app
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow these methods
                .allowedHeaders("*")  // Allow all headers (you don't need to specify "X-Session-Id" here)
                .allowCredentials(true)
                .maxAge(3600L);
    }
}

