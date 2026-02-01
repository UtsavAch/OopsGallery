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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public @NotNull UserDetails loadUserByUsername(@NotNull String email) throws UsernameNotFoundException {
        // Validation: Ensure email isn't null or blank before querying the DB
        if (email.trim().isEmpty()) {
            throw new UsernameNotFoundException("Email cannot be empty");
        }

        // Fetch user from repository
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        // Validation: Ensure the user has a role assigned to avoid NullPointerException in Security context
        if (user.getRole() == null) {
            throw new UsernameNotFoundException("User has no assigned roles/authorities");
        }

        // Convert your Enum Role to a Spring Security Authority
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

        // Return the UserPrincipal
        return new UserPrincipal(user, Collections.singletonList(authority));
    }
}