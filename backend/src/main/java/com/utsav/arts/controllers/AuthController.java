package com.utsav.arts.controllers;

import com.utsav.arts.configurations.JwtUtils;
import com.utsav.arts.dtos.loginDTO.LoginRequestDTO;
import com.utsav.arts.dtos.loginDTO.LoginResponseDTO;
import com.utsav.arts.models.User;
import com.utsav.arts.services.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @NotNull LoginRequestDTO request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtils.generateJwtToken(user.getEmail());
        return ResponseEntity.ok(new LoginResponseDTO(token, user.getId(), user.getEmail(), user.getRole()));
    }
}
