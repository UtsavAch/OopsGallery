package com.utsav.arts.repository;

import com.utsav.arts.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    User update(User user);

    Optional<User> findById(int id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    void deleteById(int id);

    boolean existsByEmail(String email);
}
