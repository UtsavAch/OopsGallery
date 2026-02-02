package com.utsav.arts.services;

import com.utsav.arts.exceptions.ResourceAlreadyExistsException;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.models.Role;
import com.utsav.arts.models.User;
import com.utsav.arts.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        //Email must be unique
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceAlreadyExistsException("User with email " + user.getEmail() + " already exists.");
        }
        // ENFORCE DEFAULT ROLE
        if (user.getRole() == null) {
            user.setRole(Role.ROLE_USER);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + user.getId()));

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPhoneNo(user.getPhoneNo());
        existingUser.setAddress(user.getAddress());

        // Email change validation
        if (!existingUser.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new ResourceAlreadyExistsException(
                        "Email " + user.getEmail() + " is already taken.");
            }
            existingUser.setEmail(user.getEmail());
        }

        // Password update
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // 🔒 ROLE IS NEVER UPDATED HERE
        return userRepository.update(existingUser);
    }

    @Override
    public User updateRole(int userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        user.setRole(role);
        return userRepository.update(user);
    }

    @Override
    public Optional<User> findById(int id) {
        // Return Optional so the Controller can decide whether to throw or return 404
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        // Updated: Use ResourceNotFoundException instead of IllegalArgumentException
        if (userRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Cannot delete: User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isUserOwner(int userId, String email) {
        return findById(userId)
                .map(user -> user.getEmail().equals(email))
                .orElse(false);
    }
}