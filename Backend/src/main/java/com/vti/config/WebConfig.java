package com.vti.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Cho phép yêu cầu từ localhost:3000
        registry.addMapping("/**") // Cấu hình cho tất cả các endpoint
                .allowedOrigins("http://localhost:3000")// Địa chỉ frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Các phương thức HTTP cho phép
                .allowedHeaders("*") // Cho phép tất cả các header
                .allowCredentials(false); // Cho phép cookie trong CORS
    }
}
