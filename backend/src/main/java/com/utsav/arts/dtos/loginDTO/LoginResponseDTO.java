package com.utsav.arts.dtos.loginDTO;

import com.utsav.arts.models.Role;

/**
 * Data Transfer Object used to send login response information to clients.
 *
 * <p><strong>Fields:</strong>
 * <ul>
 *   <li>token – JWT token for authentication</li>
 *   <li>userId – ID of the logged-in user</li>
 *   <li>email – email of the logged-in user</li>
 *   <li>role – role of the logged-in user</li>
 * </ul>
 * </p>
 */
public class LoginResponseDTO {
    private String token;
    private int userId;
    private String email;
    private Role role;

    public LoginResponseDTO(String token, int userId, String email, Role role) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    // Getters
    public String getToken() { return token; }
    public int getUserId() { return userId; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
}
