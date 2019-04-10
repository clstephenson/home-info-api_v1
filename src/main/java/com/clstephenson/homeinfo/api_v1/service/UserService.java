package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    List<User> getAll();

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    User save(User user);

    void deleteById(long id);

    boolean existsById(long id);
}