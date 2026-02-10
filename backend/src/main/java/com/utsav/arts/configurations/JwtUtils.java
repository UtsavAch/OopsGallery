package com.utsav.arts.configurations;

import com.utsav.arts.secrets.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Utility class for generating and validating JWT tokens.
 *
 * <p>Supports creating tokens, extracting email from tokens, and validating
 * expiration and integrity of JWTs.
 */
@Component
public class JwtUtils {
    // Logger to see errors in the console
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private final Key signingKey;
    private final long jwtExpirationMs;

    public JwtUtils(JwtProperties jwtProperties) {
        this.jwtExpirationMs = jwtProperties.getExpirationMs();
        this.signingKey = Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * Generates a JWT token for the given email.
     *
     * @param email the email to include in the token subject
     * @return the generated JWT token
     */
    public String generateJwtToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + jwtExpirationMs)
                )
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the email from a JWT token.
     *
     * @param token the JWT token
     * @return the email stored in the token
     */
    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates the JWT token for integrity, expiration, and correctness.
     *
     * @param token the JWT token
     * @return true if valid; false otherwise
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }  catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}