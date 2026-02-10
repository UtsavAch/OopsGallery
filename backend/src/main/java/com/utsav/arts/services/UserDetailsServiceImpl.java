package com.utsav.arts.services;

import com.utsav.arts.configurations.UserPrincipal;
import com.utsav.arts.models.User;
import com.utsav.arts.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Implementation of Spring Security's {@link UserDetailsService}.
 * Loads user-specific data during authentication.
 * Converts {@link User} entity to {@link UserPrincipal} for security context.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs UserDetailsServiceImpl with the provided UserRepository.
     *
     * @param userRepository Repository for querying users by email
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user by email for authentication purposes.
     *
     * @param email The email of the user attempting to log in
     * @return A {@link UserPrincipal} containing user details and authorities
     * @throws UsernameNotFoundException if user is not found or has no assigned roles
     */
    @Override
    public @NotNull UserDetails loadUserByUsername(@NotNull String email) throws UsernameNotFoundException {
        // Ensure email is not blank
        if (email.trim().isEmpty()) {
            throw new UsernameNotFoundException("Email cannot be empty");
        }

        // Fetch user from repository
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        // Ensure user has a role
        if (user.getRole() == null) {
            throw new UsernameNotFoundException("User has no assigned roles/authorities");
        }

        // Map Enum Role to Spring Security authority
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

        // Return a UserPrincipal for Spring Security
        return new UserPrincipal(user, Collections.singletonList(authority));
    }
}