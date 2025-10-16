package com.example.btportal.controller;

import com.example.btportal.dto.ResetPasswordReques;
import com.example.btportal.model.User;
import com.example.btportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Allow your React app to connect
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // You might have a /register or /login endpoint handled by Spring Security
    // For now, a simple save for initial users
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.registerUser(user);
    }


}