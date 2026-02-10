package com.utsav.arts.configurations;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Custom {@link org.springframework.security.core.userdetails.User} implementation
 * to include the application's user ID along with Spring Security's authentication details.
 *
 * <p>This class is used as the principal for authenticated sessions, allowing
 * easy access to the user's database ID.
 */
public class UserPrincipal extends org.springframework.security.core.userdetails.User {
    private final int id;

    /**
     * Constructs a new {@link UserPrincipal}.
     *
     * @param user the application's {@link com.utsav.arts.models.User} entity
     * @param authorities the granted authorities (roles) for the user
     */
    public UserPrincipal(com.utsav.arts.models.User user,
                         Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.id = user.getId();
    }

    /**
     * Returns the internal database ID of the user.
     *
     * @return the user's ID
     */
    public int getId() { return id; }
}