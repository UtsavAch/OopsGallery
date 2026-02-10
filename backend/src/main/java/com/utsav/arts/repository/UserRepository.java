package com.utsav.arts.repository;

import com.utsav.arts.models.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 *
 * <p>
 * Defines persistence operations and commonly used queries related
 * to application users, such as lookup by email and existence checks.
 * </p>
 */
public interface UserRepository {

    /**
     * Persists a new user entity.
     *
     * @param user the user to be saved
     * @return the persisted user
     */
    User save(User user);

    /**
     * Updates an existing user entity.
     *
     * @param user the user to update
     * @return the updated user
     */
    User update(User user);

    /**
     * Finds a user by its unique identifier.
     *
     * @param id the user ID
     * @return an {@link Optional} containing the user if found
     */
    Optional<User> findById(int id);

    /**
     * Finds a user by email address.
     *
     * <p>
     * Commonly used during authentication and user registration
     * to ensure email uniqueness.
     * </p>
     *
     * @param email the user's email address
     * @return an {@link Optional} containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all users
     */
    List<User> findAll();

    /**
     * Deletes a user by its ID.
     *
     * @param id the user ID
     */
    void deleteById(int id);

    /**
     * Checks whether a user exists with the given email address.
     *
     * @param email the email address to check
     * @return {@code true} if a user with the email exists, {@code false} otherwise
     */
    boolean existsByEmail(String email);
}
