package com.utsav.arts.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utsav.arts.services.UserDetailsServiceImpl;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;

/**
 * Spring Security configuration class.
 *
 * <p>Sets up authentication, authorization, password encoding, JWT filter, and custom error handling.
 * Uses stateless session management since authentication is handled via JWT.
 *
 * <p>Access rules:
 * <ul>
 *     <li>/api/auth/** → public</li>
 *     <li>/api/users/register, /api/users/verify, /api/users/resend-verification → public</li>
 *     <li>/api/users (POST) → OWNER only</li>
 *     <li>/api/users/** (GET) → public</li>
 *     <li>/api/stripe/webhook → public</li>
 *     <li>All other endpoints → authenticated users only</li>
 * </ul>
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper; // To write the JSON response

    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          @Lazy JwtAuthenticationFilter jwtAuthenticationFilter,
                          ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.objectMapper = objectMapper;
    }

    /**
     * Password encoder bean using BCrypt hashing.
     *
     * @return a {@link PasswordEncoder} instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures a DAO-based authentication provider using the user details service and password encoder.
     *
     * @return the configured {@link DaoAuthenticationProvider}
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Provides the {@link AuthenticationManager} bean used for authentication.
     *
     * @param authConfig the {@link AuthenticationConfiguration} provided by Spring
     * @return the {@link AuthenticationManager}
     * @throws Exception if authentication manager cannot be created
     */
    @Bean
    public AuthenticationManager authenticationManager(@NonNull AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configures HTTP security including route authorization, exception handling, session management, and JWT filter.
     *
     * @param http the {@link HttpSecurity} object to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(@NonNull HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                // --- SIMPLE ERROR HANDLING ---
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(401);
                            objectMapper.writeValue(response.getOutputStream(),
                                    Map.of("status", 401, "error", "Unauthorized", "message", "Please login first"));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(403);
                            objectMapper.writeValue(response.getOutputStream(),
                                    Map.of("status", 403, "error", "Forbidden", "message", "You don't have permission"));
                        })
                )
                // -----------------------------
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/artworks/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/verify").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/resend-verification").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                        .requestMatchers("/api/stripe/webhook").permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}