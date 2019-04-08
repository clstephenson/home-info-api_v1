package com.clstephenson.homeinfo.api_v1.controller;

import com.clstephenson.homeinfo.api_v1.exception.UserNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Find All
    @GetMapping("/users")
    Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Save
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    User createUser(@Valid @RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    // Find by ID
    @GetMapping("/users/{userId}")
    User findUser(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    // Update User
    @PutMapping("/users/{userId}")
    User updateUser(@PathVariable Long userId, @Valid @RequestBody User userRequest) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setFirstName(userRequest.getFirstName());
                    user.setLastName(userRequest.getLastName());
                    user.setEmail(userRequest.getEmail());
                    user.setPassword(userRequest.getPassword());
                    user.setDisabled(userRequest.getDisabled());
                    return userRepository.save(user);
                }).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @DeleteMapping("/users/{userId}")
    ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return userRepository.findById(userId).map(user -> {
            userRepository.deleteById(userId);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
