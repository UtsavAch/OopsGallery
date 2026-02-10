package com.utsav.arts.dtos.userDTO;

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
