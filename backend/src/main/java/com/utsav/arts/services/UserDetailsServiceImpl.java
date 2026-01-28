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
        // Fetch user from your repository
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        // Convert your Enum Role to a Spring Security Authority
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

        // Return a Spring Security User object
        return new UserPrincipal(user, Collections.singletonList(authority));
    }
}