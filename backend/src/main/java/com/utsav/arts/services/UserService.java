package com.utsav.arts.services;

import com.utsav.arts.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    User update(User user);

    Optional<User> findById(int id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    void deleteById(int id);

    boolean existsByEmail(String email);
}
