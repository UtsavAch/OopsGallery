package com.utsav.arts.controllers;

import com.utsav.arts.dtos.userDTO.UserRequestDTO;
import com.utsav.arts.dtos.userDTO.UserResponseDTO;
import com.utsav.arts.mappers.UserMapper;
import com.utsav.arts.models.User;
import com.utsav.arts.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserResponseDTO> save(@RequestBody UserRequestDTO requestDTO) {
        User user = UserMapper.toEntity(requestDTO);
        User savedUser = userService.save(user);
        return new ResponseEntity<>(
                UserMapper.toResponseDTO(savedUser),
                HttpStatus.CREATED
        );
    }

    // ---------------- UPDATE ----------------
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable int id,
            @RequestBody UserRequestDTO requestDTO
    ) {
        User user = UserMapper.toEntity(requestDTO);
        user.setId(id);

        User updatedUser = userService.update(user);
        return ResponseEntity.ok(UserMapper.toResponseDTO(updatedUser));
    }

    // ---------------- READ ----------------
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable int id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(UserMapper.toResponseDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> findByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(UserMapper.toResponseDTO(user)))
                .orElse(ResponseEntity.notFound().build());
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
