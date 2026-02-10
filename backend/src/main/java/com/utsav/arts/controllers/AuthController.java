package com.utsav.arts.controllers;

import com.utsav.arts.configurations.JwtUtils;
import com.utsav.arts.dtos.loginDTO.LoginRequestDTO;
import com.utsav.arts.dtos.loginDTO.LoginResponseDTO;
import com.utsav.arts.exceptions.InvalidRequestException;
import com.utsav.arts.models.User;
import com.utsav.arts.services.UserService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for user authentication.
 *
 * <p>Currently supports:
 * <ul>
 *     <li>POST /api/auth/login → Authenticate user and return JWT token</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Authenticates a user using email and password.
     *
     * <p>If authentication is successful, returns a JWT token along with user details.
     * On failure, throws InvalidRequestException with a generic "Invalid credentials" message.
     *
     * @param request LoginRequestDTO containing email and password
     * @return LoginResponseDTO containing JWT token and user info
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody @NotNull LoginRequestDTO request) {
        // 1. Check if user exists by email
        // Note: For security, we use a generic "Invalid credentials" message
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidRequestException("Invalid email or password"));

        // 2. Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidRequestException("Invalid email or password");
        }

        // 3. Generate Token
        String token = jwtUtils.generateJwtToken(user.getEmail());

        return ResponseEntity.ok(new LoginResponseDTO(
                token,
                user.getId(),
                user.getEmail(),
                user.getRole()
        ));
    }
}