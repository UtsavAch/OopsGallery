package com.utsav.arts.controllers;

import com.utsav.arts.dtos.userDTO.ResendVerificationDTO;
import com.utsav.arts.dtos.userDTO.UserRequestDTO;
import com.utsav.arts.dtos.userDTO.UserResponseDTO;
import com.utsav.arts.dtos.userDTO.VerifyRegistrationDTO;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.mappers.UserMapper;
import com.utsav.arts.models.Role;
import com.utsav.arts.models.User;
import com.utsav.arts.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ---------------- CREATE (OWNER ONLY) ----------------
    // Restricted: Only Owners can create users directly (bypassing email verification)
    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<UserResponseDTO> save(@Valid @RequestBody UserRequestDTO requestDTO) {
        User user = UserMapper.toEntity(requestDTO);
        User savedUser = userService.save(user);
        return new ResponseEntity<>(
                UserMapper.toResponseDTO(savedUser),
                HttpStatus.CREATED
        );
    }

    // ---------------- CREATE / REGISTER ----------------
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequestDTO requestDTO) {
        User user = UserMapper.toEntity(requestDTO);
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully. Please check your email for the verification code.");
    }

    // ---------------- VERIFY ----------------
    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody VerifyRegistrationDTO request) {
        boolean isVerified = userService.verifyUser(
                request.getEmail(),
                request.getCode()
        );

        if (isVerified) {
            return ResponseEntity.ok("Account verified successfully!");
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid or expired verification code.");
        }
    }

    // ---------------- RESEND VERIFICATION ----------------
    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(
            @RequestBody @Valid ResendVerificationDTO request
    ) {
        userService.resendVerification(request.getEmail());
        return ResponseEntity.ok("Verification code resent successfully.");
    }

    // ---------------- UPDATE ----------------
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable int id,
            @Valid @RequestBody UserRequestDTO requestDTO
    ) {
        User user = UserMapper.toEntity(requestDTO);
        user.setId(id);
        User updatedUser = userService.update(user);
        return ResponseEntity.ok(UserMapper.toResponseDTO(updatedUser));
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<UserResponseDTO> updateRole(
            @PathVariable int id,
            @RequestParam Role role
    ) {
        User updated = userService.updateRole(id, role);
        return ResponseEntity.ok(UserMapper.toResponseDTO(updated));
    }

    // ---------------- READ ----------------

    // Only the Owner can see the full list of users
    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<UserResponseDTO> users = userService.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    // Owner can see anyone; User can only see themselves
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable int id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return ResponseEntity.ok(UserMapper.toResponseDTO(user));
    }

    // Owner can search anyone; User can only search their own email
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('OWNER') or #email == authentication.principal.username")
    public ResponseEntity<UserResponseDTO> findByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return ResponseEntity.ok(UserMapper.toResponseDTO(user));
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or #id == authentication.principal.id")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- UTILITY ----------------
    // Public: Often needed during registration to check if email is taken
    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }
}