package com.utsav.arts.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utsav.arts.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Filter that handles JWT-based authentication for incoming requests.
 *
 * <p>Extracts the JWT token from the "Authorization" header, validates it, and
 * sets the {@link SecurityContextHolder} if the token is valid.
 *
 * <p>Invalid or expired tokens result in a 401 Unauthorized response with a JSON body.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper; // To convert Map to JSON

    public JwtAuthenticationFilter(JwtUtils jwtUtils,
                                   UserDetailsServiceImpl userDetailsService,
                                   ObjectMapper objectMapper) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    /**
     * Filters each HTTP request to validate JWT tokens.
     *
     * <p>If a valid token is present, the user is authenticated for the current request.
     * Otherwise, the request is either passed through (for public routes) or rejected.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException in case of servlet errors
     * @throws IOException in case of I/O errors
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Ignore preflight
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }

            String authHeader = request.getHeader("Authorization");

            // No Token found - let it pass (SecurityConfig will decide if the route is public or private)
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = authHeader.substring(7);

            // Validate Token
            if (jwtUtils.validateJwtToken(jwt)) {
                String email = jwtUtils.getEmailFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // If token is invalid, we clear context and return 401
                handleException(response, "Invalid or expired JWT token", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            filterChain.doFilter(request, response);

        } catch (UsernameNotFoundException e) {
            handleException(response, "User not found associated with this token", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            // Catching any other security-related errors
            handleException(response, "Authentication error: " + e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    /**
     * Sends a JSON error response and clears the security context.
     *
     * @param response the HTTP response
     * @param message the error message
     * @param status the HTTP status code
     * @throws IOException if writing to response fails
     */
    private void handleException(HttpServletResponse response, String message, int status) throws IOException {
        SecurityContextHolder.clearContext();
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new HashMap<>();
        body.put("status", status);
        body.put("error", "Unauthorized");
        body.put("message", message);

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}