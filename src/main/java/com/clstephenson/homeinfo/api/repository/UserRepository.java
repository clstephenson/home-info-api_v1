package com.clstephenson.homeinfo.api.repository;

import com.clstephenson.homeinfo.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
