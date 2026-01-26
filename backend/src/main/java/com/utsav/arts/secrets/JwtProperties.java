package com.utsav.arts.secrets;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

// Just for verification (no need to change if it looks like this)
@Component
@ConfigurationProperties(prefix = "jwt") // matches "jwt" in application.properties
@Validated
public class JwtProperties {

    @NotBlank
    private String secret; // maps to "jwt.secret"

    @Min(60000)
    private long expirationMs; // maps to "jwt.expiration-ms"

    // Getters and Setters are REQUIRED for this to work
    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public long getExpirationMs() { return expirationMs; }
    public void setExpirationMs(long expirationMs) { this.expirationMs = expirationMs; }
}
