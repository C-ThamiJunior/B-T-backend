package com.example.btportal.model;

import jakarta.persistence.*; // Using jakarta for Spring Boot 3+
import lombok.Data; // Lombok annotation for generating boilerplate code (getters, setters, equals, hashCode, toString)

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data // Lombok: Generates getters, setters, toString, equals, and hashCode methods
@Table(name = "users") // Specifies the table name in the database
public class User {
    @Id // Marks the field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures the primary key generation strategy
    private Long id; // Unique identifier for the user

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String surname;

    @Column(unique = true, nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private String password; // Hashed password for security

    @Column(unique = true, nullable = false) // Ensures email is unique and not null
    private String email; // User's email address

    @Enumerated(EnumType.STRING) // Stores the enum name (e.g., "STUDENT") as a string in the DB
    @Column(nullable = false)
    private Role role; // User's role (e.g., STUDENT, INSTRUCTOR, ADMIN)

    private LocalDateTime lastLogin;
    private Date creationDate;

    @PrePersist
    protected void onCreate() {
        this.creationDate = new Date();
    }
}
