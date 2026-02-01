package com.utsav.arts.controllers;

import com.utsav.arts.dtos.userDTO.UserRequestDTO;
import com.utsav.arts.dtos.userDTO.UserResponseDTO;
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

    // ---------------- CREATE ----------------
    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@Valid @RequestBody UserRequestDTO requestDTO) {
        User user = UserMapper.toEntity(requestDTO);
        user.setRole(Role.ROLE_USER);
        User savedUser = userService.save(user);
        return new ResponseEntity<>(
                UserMapper.toResponseDTO(savedUser),
                HttpStatus.CREATED
        );
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

    // ---------------- READ ----------------
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable int id) {
        // Proper: Throwing exception triggers GlobalExceptionHandler's JSON response
        User user = userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return ResponseEntity.ok(UserMapper.toResponseDTO(user));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> findByEmail(@PathVariable String email) {
        // Proper: Throwing exception triggers GlobalExceptionHandler's JSON response
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return ResponseEntity.ok(UserMapper.toResponseDTO(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<UserResponseDTO> users = userService.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or #id == authentication.principal.id")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- UTILITY ----------------
    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }
}