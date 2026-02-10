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

/**
 * Controller to manage users.
 *
 * <p>Supports user registration, verification, creating users, reading user details,
 * updating user information and roles, deleting users, and checking email existence.
 * Access is restricted according to user roles (OWNER or the user themselves).
 *
 * <p>Endpoints:
 * <ul>
 *     <li>POST /api/users → Create a user directly (OWNER only)</li>
 *     <li>POST /api/users/register → Register a new user (with email verification)</li>
 *     <li>POST /api/users/verify → Verify a user account with code</li>
 *     <li>POST /api/users/resend-verification → Resend verification code</li>
 *     <li>PUT /api/users/{id} → Update user details (OWNER or self)</li>
 *     <li>PATCH /api/users/{id}/role → Update user role (OWNER only)</li>
 *     <li>GET /api/users → Get all users (OWNER only)</li>
 *     <li>GET /api/users/{id} → Get user by ID (OWNER or self)</li>
 *     <li>GET /api/users/email/{email} → Get user by email (OWNER or self)</li>
 *     <li>DELETE /api/users/{id} → Delete user (OWNER or self)</li>
 *     <li>GET /api/users/exists/{email} → Check if email exists</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ---------------- CREATE (OWNER ONLY) ----------------
    /**
     * Creates a new user directly. Only accessible by OWNER.
     *
     * <p>This bypasses email verification.
     *
     * @param requestDTO DTO containing user information
     * @return {@link UserResponseDTO} of the newly created user
     */
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
    /**
     * Registers a new user. Sends a verification email.
     *
     * @param requestDTO DTO containing user registration information
     * @return Confirmation message
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequestDTO requestDTO) {
        User user = UserMapper.toEntity(requestDTO);
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully. Please check your email for the verification code.");
    }

    // ---------------- VERIFY ----------------
    /**
     * Verifies a user account using the email and verification code.
     *
     * @param request DTO containing email and verification code
     * @return Success or error message
     */
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
    /**
     * Resends a verification code to the user's email.
     *
     * @param request DTO containing the user's email
     * @return Confirmation message
     */
    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(
            @RequestBody @Valid ResendVerificationDTO request
    ) {
        userService.resendVerification(request.getEmail());
        return ResponseEntity.ok("Verification code resent successfully.");
    }

    // ---------------- UPDATE ----------------
    /**
     * Updates a user's information. Accessible by OWNER or the user themselves.
     *
     * @param id ID of the user to update
     * @param requestDTO DTO containing updated user information
     * @return {@link UserResponseDTO} of the updated user
     */
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

    /**
     * Updates a user's role. Only accessible by OWNER.
     *
     * @param id ID of the user
     * @param role New role to assign
     * @return {@link UserResponseDTO} of the updated user
     */
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
    /**
     * Retrieves all users. OWNER only.
     *
     * @return List of {@link UserResponseDTO}
     */
    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<UserResponseDTO> users = userService.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a user by ID. Accessible by OWNER or the user themselves.
     *
     * @param id User ID
     * @return {@link UserResponseDTO} of the user
     * @throws ResourceNotFoundException if user not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable int id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return ResponseEntity.ok(UserMapper.toResponseDTO(user));
    }

    /**
     * Retrieves a user by email. Accessible by OWNER or the user themselves.
     *
     * @param email User email
     * @return {@link UserResponseDTO} of the user
     * @throws ResourceNotFoundException if user not found
     */
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('OWNER') or #email == authentication.principal.username")
    public ResponseEntity<UserResponseDTO> findByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return ResponseEntity.ok(UserMapper.toResponseDTO(user));
    }

    /**
     * Deletes a user by ID. Accessible by OWNER or the user themselves.
     *
     * @param id User ID
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or #id == authentication.principal.id")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- UTILITY ----------------
    /**
     * Checks if an email is already registered in the system.
     *
     * @param email Email to check
     * @return {@code true} if email exists, {@code false} otherwise
     */
    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }
}