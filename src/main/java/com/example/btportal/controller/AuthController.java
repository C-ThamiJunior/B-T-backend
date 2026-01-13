package com.example.btportal.controller;

import com.example.btportal.dto.ResetPasswordReques;
import com.example.btportal.dto.request.LoginRequest;
import com.example.btportal.dto.request.RegisterRequest;
import com.example.btportal.model.User;
import com.example.btportal.security.JwtUtil;
import com.example.btportal.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// ✅ Import these
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager; // ✅ ADD THIS

    // ✅ UPDATE THE CONSTRUCTOR
    public AuthController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        // ... (this method is fine as is)
        try {
            User newUser = new User();
            newUser.setFirstname(registerRequest.firstname);
            newUser.setSurname(registerRequest.surname);
            newUser.setContactNumber(registerRequest.contactNumber);
            newUser.setEmail(registerRequest.email);
            newUser.setPassword(registerRequest.password);
            newUser.setRole(registerRequest.role);

            User registeredUser = userService.registerUser(newUser);

            Map<String, Object> response = new HashMap<>();
            response.put("id", registeredUser.getId());
            response.put("firstname", registeredUser.getFirstname());
            response.put("surname", registeredUser.getSurname());
            response.put("contactNumber", registeredUser.getContactNumber());
            response.put("email", registeredUser.getEmail());
            response.put("role", registeredUser.getRole());

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // ✅ REPLACE YOUR LOGIN METHOD WITH THIS
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {

        // This will automatically use your UserDetailsServiceImpl and PasswordEncoder
        // It throws an exception if authentication fails
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }

        // If authentication is successful, find the user and generate the token
        // We know the user exists because authentication passed
        User user = userService.findByEmail(loginRequest.getEmail()).get();

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
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordReques request) {
        // ... (this method is fine as is)
        userService.resetPassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully");
    }
}