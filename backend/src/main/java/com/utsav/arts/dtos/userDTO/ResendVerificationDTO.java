package com.utsav.arts.dtos.userDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO used when a user requests to resend the verification email.
 *
 * <p><strong>Fields:</strong>
 * <ul>
 *   <li>email – email address of the user to resend the verification code to</li>
 * </ul>
 */
public class ResendVerificationDTO {

    @NotBlank
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
