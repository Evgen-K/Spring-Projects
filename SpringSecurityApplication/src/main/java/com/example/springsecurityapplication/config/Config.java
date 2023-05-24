package com.example.springsecurityapplication.config;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Если где-то встречается такой путь - "/img/**", то необходимо идти
        // по пути -> "file:///" + uploadPath + "/"
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///" + uploadPath + "/");

    }
}