package com.utsav.arts.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Jackson.
 *
 * <p>Provides a singleton {@link ObjectMapper} bean for JSON serialization
 * and deserialization throughout the application.
 */
@Configuration
public class JacksonConfig {

    /**
     * Creates a default {@link ObjectMapper} bean.
     *
     * @return the Jackson {@link ObjectMapper} instance
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}