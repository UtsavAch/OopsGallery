package com.utsav.arts.secrets;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for JWT (JSON Web Token) authentication.
 *
 * <p>
 * This class binds properties prefixed with {@code jwt} from
 * {@code application.properties} or {@code application.yml}
 * into a strongly typed configuration object.
 * </p>
 *
 * <p>
 * Example configuration:
 * <pre>
 * jwt.secret=your-secret-key
 * jwt.expiration-ms=3600000
 * </pre>
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "jwt") // matches "jwt" in application.properties
@Validated
public class JwtProperties {

    @NotBlank
    private String secret; // maps to "jwt.secret"

    @Min(60000)
    private long expirationMs; // maps to "jwt.expiration-ms"

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public long getExpirationMs() { return expirationMs; }
    public void setExpirationMs(long expirationMs) { this.expirationMs = expirationMs; }
}
