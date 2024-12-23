package com.example.swyp_team1_back.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {  // cors 에러 해결
        registry.addMapping("/**")

                .allowedOrigins("https://actip.swygbro.com" , "https://actip.site", "http://localhost:8080", "http://localhost:3000")
                .allowedMethods("OPTIONS","GET","POST","PUT","DELETE","PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
