package com.utsav.arts.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Configuration class for REST client.
 *
 * <p>Provides a {@link RestClient.Builder} bean for making HTTP requests
 * to external services like Supabase or other APIs.
 */
@Configuration
public class RestClientConfig {

    /**
     * Provides a {@link RestClient.Builder} for creating {@link RestClient} instances.
     *
     * @return a new {@link RestClient.Builder} instance
     */
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}