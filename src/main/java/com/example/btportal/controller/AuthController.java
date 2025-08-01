package com.example.btportal.controller;

import com.example.btportal.dto.request.LoginRequest;
import com.example.btportal.dto.request.RegisterRequest;
import com.example.btportal.model.User;
import com.example.btportal.security.JwtUtil;
import com.example.btportal.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; // For input validation

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * REST ApplicationController for user authentication and registration.
 * Handles API requests related to user login and new user registration.
 */
@RestController // Marks this class as a REST ApplicationController, handling incoming REST requests
@RequestMapping("/api/auth") // Base path for all endpoints in this controller
@CrossOrigin(origins = "*") // Allows requests from the React frontend running on localhost:3000
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;


    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Handles user registration.
     * @param registerRequest The request body containing username, password, and email.
     * @return ResponseEntity with success message and created user details or error message.
     */
    @PostMapping("/register") // Maps HTTP POST requests to /api/auth/register
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User newUser = new User();
            newUser.setFirstname(registerRequest.firstname);
            newUser.setSurname(registerRequest.surname);
            newUser.setContactNumber(registerRequest.contactNumber);
            newUser.setEmail(registerRequest.email);
            newUser.setPassword(registerRequest.password); // Password will be hashed in UserService
            // If a role is provided in the request, use it; otherwise, UserService will default to STUDENT
            newUser.setRole(registerRequest.role);

            User registeredUser = userService.registerUser(newUser);

            // Prepare response, omitting sensitive information like the password
            Map<String, Object> response = new HashMap<>();
            response.put("id", registeredUser.getId());
            response.put("firstname", registeredUser.getFirstname());
            response.put("surname", registeredUser.getSurname());
            response.put("contactNumber", registeredUser.getContactNumber());
            response.put("email", registeredUser.getEmail());
            response.put("role", registeredUser.getRole()); // Include role in response

            return new ResponseEntity<>(response, HttpStatus.CREATED); // Return 201 Created status
        } catch (RuntimeException e) {
            // Catch specific exceptions from service (e.g., username/email taken)
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST); // Return 400 Bad Request
        }
    }

    /**
     * Handles user login.
     * @param loginRequest The request body containing username and password.
     * @return ResponseEntity with authentication token/user details or error message.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<User> authenticatedUser = userService.authenticateUser(
                loginRequest.getEmail(), loginRequest.getPassword());

        return authenticatedUser
                .map(user -> {
                    String token = jwtUtil.generateToken(
                            user,
                            List.of("ROLE_" + user.getRole())
                    );

                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Login successful");
                    response.put("user", Map.of(
                            "id", user.getId(),
                            "firstname", user.getFirstname(),
                            "email", user.getEmail(),
                            "role", user.getRole(),
                            "token", token
                    ));

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid email or password")));
    }
}

