package com.example.btportal.service;

// User Service
// src/main/java/com/lms/service/UserService.java

import com.example.btportal.model.Role;
import com.example.btportal.model.User;
import com.example.btportal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import for transactional operations

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing User-related business logic.
 * Interacts with the UserRepository to perform database operations.
 */
@Service // Marks this class as a Spring Service component
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Injected for password hashing

    /**
     * Constructor for dependency injection.
     * Spring automatically injects UserRepository and PasswordEncoder.
     * @param userRepository The repository for User entities.
     * @param passwordEncoder The encoder for hashing passwords.
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves all users from the database.
     * @return A list of all User entities.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     * @param id The ID of the user.
     * @return An Optional containing the User if found, or empty if not.
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Registers a new user.
     * Validates uniqueness of username and email, hashes the password, and saves the user.
     * @param user The User object containing registration details.
     * @return The saved User entity.
     * @throws RuntimeException if username or email is already taken.
     */
    @Transactional // Ensures the entire method runs within a single database transaction
    public User registerUser(User user) {
        if (userRepository.existsByContactNumber(user.getContactNumber())) {
            throw new RuntimeException("Contact number already registered.");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered.");
        }
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Set default role if not explicitly provided (e.g., during admin creation)
        if (user.getRole() == null) {
            user.setRole(Role.ADMIN); // Dehttps://application-admin.onrender.com/registerfault role for new registrations
        }
        return userRepository.save(user);
    }

    /**
     * Authenticates a user based on username and raw password.
     * Compares the provided raw password with the stored hashed password.
     * In a real application, this would be part of Spring Security's authentication flow
     * and would typically involve returning a JWT.
     * @param email The email provided by the user.
     * @param rawPassword The plain text password provided by the user.
     * @return An Optional containing the authenticated User if credentials are valid, or empty otherwise.
     */
    public Optional<User> authenticateUser(String email, String rawPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Compare the raw password with the hashed password using the PasswordEncoder
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Transactional
    public User resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Encode new password
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
