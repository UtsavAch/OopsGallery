package com.utsav.arts.services;

import com.utsav.arts.exceptions.ResourceAlreadyExistsException;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.models.Role;
import com.utsav.arts.models.User;
import com.utsav.arts.models.VerificationCode;
import com.utsav.arts.repository.UserRepository;
import com.utsav.arts.repository.VerificationCodeRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationCodeRepository codeRepository;
    @Autowired
    private EmailService emailService;

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
        user.setEnabled(true); // Admins create active users by default
        return userRepository.save(user);
    }

    // REGISTER USER
    @Override
    public User registerUser(User user) {

        Optional<User> existingOpt = userRepository.findByEmail(user.getEmail());

        if (existingOpt.isPresent()) {
            User existing = existingOpt.get();

            if (existing.isEnabled()) {
                throw new RuntimeException("Email already taken");
            }

            // User exists but NOT verified → resend code
            resendVerification(existing.getEmail());
            return existing;
        }

        user.setRole(Role.ROLE_USER);
        user.setEnabled(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        SecureRandom random = new SecureRandom();
        String rawCode = String.valueOf(100000 + random.nextInt(900000));
        String hashedCode = passwordEncoder.encode(rawCode);

        VerificationCode verificationCode = new VerificationCode(hashedCode, savedUser);
        savedUser.setVerificationCode(verificationCode);

        codeRepository.save(verificationCode);
        emailService.sendVerificationEmail(savedUser.getEmail(), rawCode);

        return savedUser;
    }

    // VERIFY USER
    @Override
    public boolean verifyUser(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VerificationCode vCode = codeRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("No verification code found"));

        if (passwordEncoder.matches(code, vCode.getCode()) &&
                vCode.getExpiryDate().isAfter(LocalDateTime.now())) {

            user.setEnabled(true);
            userRepository.save(user);

            // OTP auto-deleted because of orphanRemoval
            user.setVerificationCode(null);

            return true;
        }
        return false;
    }

    @Override
    public void resendVerification(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email));

        if (user.isEnabled()) {
            throw new RuntimeException("User is already verified");
        }

        SecureRandom random = new SecureRandom();
        String rawCode = String.valueOf(100000 + random.nextInt(900000));
        String hashedCode = passwordEncoder.encode(rawCode);

        VerificationCode verificationCode =
                codeRepository.findByUser(user)
                        .orElseGet(() -> new VerificationCode(hashedCode, user));

        verificationCode.setCode(hashedCode);
        verificationCode.setExpiryDate(LocalDateTime.now().plusMinutes(10));

        codeRepository.save(verificationCode);

        emailService.sendVerificationEmail(user.getEmail(), rawCode);
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

        // ROLE IS NEVER UPDATED HERE
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