package com.utsav.arts.configurations;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserPrincipal extends org.springframework.security.core.userdetails.User {
    private final int id;

    public UserPrincipal(com.utsav.arts.models.User user,
                         Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.id = user.getId();
    }

    public int getId() { return id; }
}