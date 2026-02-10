package com.utsav.arts.services;

import com.utsav.arts.models.Role;
import com.utsav.arts.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    User registerUser(User user);

    boolean verifyUser(String email, String code);

    User update(User user);

    User updateRole(int userId, Role role);

    Optional<User> findById(int id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    void deleteById(int id);

    boolean existsByEmail(String email);

    boolean isUserOwner(int userId, String email);

    void resendVerification(String email);

}
