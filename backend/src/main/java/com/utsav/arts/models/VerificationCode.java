package com.utsav.arts.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a verification code used for confirming a user's email.
 * The code expires after a certain period.
 */
@Entity
@Table(name = "verification_codes")
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String code;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    /** Default constructor for JPA */
    public VerificationCode() {}

    /**
     * Constructs a verification code for a user.
     * Sets expiry time to 15 minutes from creation.
     *
     * @param code The verification code
     * @param user The user associated with the code
     */
    public VerificationCode(String code, User user) {
        this.code = code;
        this.user = user;
        this.expiryDate = LocalDateTime.now().plusMinutes(15);
    }

    public String getCode() { return code; }

    public void setCode(String code) {
        this.code = code;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public User getUser() { return user; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
}
