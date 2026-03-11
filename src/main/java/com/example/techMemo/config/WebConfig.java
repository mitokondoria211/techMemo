package com.example.techMemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("api/v1/**")
                .allowedOrigins("https://domain1.com", "https://domain2.com")
                .allowedMethods("GET", "PUT")
                .allowedHeaders("header1", "header2", "header3")
                .exposedHeaders("header1", "header2")
                .allowCredentials(true)
                .maxAge(3600);

        // Add more mappings...
    }
}
