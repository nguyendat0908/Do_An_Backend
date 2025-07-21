package com.DatLeo.BookShop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Cho phép các URL nào có thể truy cập tới Backend
        corsConfiguration.setAllowedOrigins(
                Arrays.asList("http://localhost:3000"));

        // Các method nào được kết nối
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Các phần Header được phép gửi lên
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "x-no-retry"));

        // Gửi kèm cookie hay không?
        corsConfiguration.setAllowCredentials(true);

        // Thời gian pre-flight request có thể cache (tính theo seconds)
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Cấu hình cors cho tất cả api
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
