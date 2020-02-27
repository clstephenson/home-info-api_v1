package com.clstephenson.homeinfo.api.service;

import com.clstephenson.homeinfo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    User save(User user);

    void deleteById(long id);

    boolean existsById(long id);
}