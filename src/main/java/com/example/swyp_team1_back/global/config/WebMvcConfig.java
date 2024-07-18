package com.example.swyp_team1_back.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://swyg-front.vercel.app" , "http://15.164.202.203:8080", "http://15.164.202.203", "http://localhost:3000")
                .allowedMethods("OPTIONS","GET","POST","PUT","DELETE","PATCH");
    }
}