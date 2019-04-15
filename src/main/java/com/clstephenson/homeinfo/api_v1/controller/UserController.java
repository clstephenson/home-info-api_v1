package com.clstephenson.homeinfo.api_v1.controller;

import com.clstephenson.homeinfo.api_v1.exception.UserNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // Find All
    @GetMapping("/users")
    List<User> getAllUsers() {
        return userService.getAll();
    }

    // Save
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    User createUser(@Valid @RequestBody User newUser) {
        return userService.save(newUser);
    }

    // Find by ID
    @GetMapping("/users/{userId}")
    User findUser(@PathVariable Long userId) {
        return userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    // Update User
    @PutMapping("/users/{userId}")
    User updateUser(@PathVariable Long userId, @Valid @RequestBody User userRequest) {
        return userService.findById(userId)
                .map(user -> {
                    user.setFirstName(userRequest.getFirstName());
                    user.setLastName(userRequest.getLastName());
                    user.setEmail(userRequest.getEmail());
                    user.setPassword(userRequest.getPassword());
                    user.setDisabled(userRequest.getDisabled());
                    return userService.save(user);
                }).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @DeleteMapping("/users/{userId}")
    ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return userService.findById(userId).map(user -> {
            userService.deleteById(userId);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
