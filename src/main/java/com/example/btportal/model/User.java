package com.example.btportal.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore; // ✅ Import this for the fix

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List; // ✅ Import List

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String surname;

    @Column(unique = true, nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime lastLogin;
    private Date creationDate;

    // --- RELATIONSHIPS (The cause of your 500 Error) ---

    // 1. A User has many Enrollments
    // @JsonIgnore stops the loop: User -> Enrollment -> User -> Enrollment...
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Enrollment> enrollments;

    // 2. A User (Student) has many Submissions
    @OneToMany(mappedBy = "student") // Ensure this matches 'private User student' in AssignmentSubmission
    @JsonIgnore
    private List<AssignmentSubmission> submissions;

    // 3. A User (Facilitator) might have created courses
    // Optional: Only needed if you want to see "Courses Created By Me" from the User side
    @OneToMany(mappedBy = "facilitator")
    @JsonIgnore
    private List<Course> coursesCreated;

    @PrePersist
    protected void onCreate() {
        this.creationDate = new Date();
    }

    public String getFullName() {
        return firstname + " " + surname;
    }
}