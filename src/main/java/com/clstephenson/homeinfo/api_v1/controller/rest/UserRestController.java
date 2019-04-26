package com.clstephenson.homeinfo.api_v1.controller.rest;

import com.clstephenson.homeinfo.api_v1.exception.UserNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.model.UserList;
import com.clstephenson.homeinfo.api_v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    // Find All
    @GetMapping("/apiv1/users")
    ResponseEntity<UserList> getAllUsers() {
        UserList userList = new UserList();
        userService.getAll().forEach(user -> userList.getUsers().add(user));
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=utf-8")
                .body(userList);
    }

    @GetMapping("/apiv1/users/{userId}")
    User findUserById(@PathVariable Long userId) {
        return userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/apiv1/users")
    User createUser(@Valid @RequestBody User newUser) {
        return userService.save(newUser);
    }

    // Update User
    @PutMapping("/apiv1/users/{userId}")
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

    @DeleteMapping("/apiv1/users/{userId}")
    ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return userService.findById(userId).map(user -> {
            userService.deleteById(userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
