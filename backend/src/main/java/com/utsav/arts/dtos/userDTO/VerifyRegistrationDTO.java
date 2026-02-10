package com.utsav.arts.dtos.userDTO;

/**
 * DTO used when verifying a user's registration with a code.
 *
 * <p><strong>Fields:</strong>
 * <ul>
 *   <li>email – email of the user</li>
 *   <li>code – verification code sent to the user</li>
 * </ul>
 */
public class VerifyRegistrationDTO {
    private String email;
    private String code;

    public VerifyRegistrationDTO() {}

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
