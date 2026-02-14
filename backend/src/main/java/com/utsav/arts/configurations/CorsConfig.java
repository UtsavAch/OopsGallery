package com.utsav.arts.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

/**
 * Configuration class for Cross-Origin Resource Sharing (CORS) in the application.
 *
 * <p>This class defines a {@link CorsConfigurationSource} bean that allows the backend
 * to accept requests from specified frontend origins, with configurable HTTP methods,
 * headers, and credentials support.
 *
 * <p>The allowed origins are injected from the environment or application properties
 * using the {@code @Value} annotation.
 */
@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    /**
     * Defines a {@link CorsConfigurationSource} bean for Spring Security.
     *
     * <p>This configuration allows:
     * <ul>
     *     <li>Requests from the origin(s) specified by {@link #allowedOrigins}</li>
     *     <li>HTTP methods: GET, POST, PUT, PATCH, DELETE, OPTIONS</li>
     *     <li>Any headers from the client</li>
     *     <li>Credentials such as cookies or authorization headers</li>
     * </ul>
     *
     * <p>The configuration is registered for all URL paths ("/**") in the application.
     *
     * @return a {@link CorsConfigurationSource} that can be used by Spring Security
     *         to apply CORS rules to incoming requests
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of(allowedOrigins));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
