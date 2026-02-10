package com.utsav.arts.services;

import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.models.Role;
import com.utsav.arts.models.User;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing users.
 * Handles creation, registration, verification, updates, role management, and deletion of users.
 */
public interface UserService {

    /**
     * Saves a user (typically by admin).
     * <p>
     * - Enforces unique email.
     * - Sets default role if not provided.
     * - Password is encoded.
     *
     * @param user User entity to save
     * @return Saved user
     */
    User save(User user);

    /**
     * Registers a new user.
     * <p>
     * - Sets role to ROLE_USER.
     * - User is initially disabled.
     * - Generates verification code and sends verification email.
     *
     * @param user User entity to register
     * @return Registered user
     */
    User registerUser(User user);

    /**
     * Verifies a user with a given email and code.
     * <p>
     * - Checks code validity and expiry.
     * - Enables user if verification succeeds.
     *
     * @param email User's email
     * @param code  Verification code
     * @return true if verification succeeds, false otherwise
     */
    boolean verifyUser(String email, String code);

    /**
     * Updates user details (excluding role).
     *
     * @param user User entity with updated fields
     * @return Updated user
     */
    User update(User user);

    /**
     * Updates the role of a user (admin only).
     *
     * @param userId ID of the user
     * @param role   New role
     * @return Updated user
     */
    User updateRole(int userId, Role role);

    /**
     * Finds a user by ID.
     *
     * @param id User ID
     * @return Optional containing user if found, else empty
     */
    Optional<User> findById(int id);

    /**
     * Finds a user by email.
     *
     * @param email User email
     * @return Optional containing user if found, else empty
     */
    Optional<User> findByEmail(String email);

    /**
     * Returns a list of all users.
     *
     * @return List of users
     */
    List<User> findAll();

    /**
     * Deletes a user by ID.
     *
     * @param id User ID
     * @throws ResourceNotFoundException if user does not exist
     */
    void deleteById(int id);

    /**
     * Checks if a user exists by email.
     *
     * @param email User email
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Checks if a user with given ID owns the provided email.
     *
     * @param userId User ID
     * @param email  Email to check
     * @return true if email belongs to the user, false otherwise
     */
    boolean isUserOwner(int userId, String email);

    /**
     * Resends verification code to the user's email.
     *
     * @param email User email
     */
    void resendVerification(String email);

}
