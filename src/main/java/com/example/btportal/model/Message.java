package com.example.btportal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    // ✅ CRITICAL: Ignore User fields that cause loops
    @JsonIgnoreProperties({"enrollments", "submissions", "coursesCreated", "password", "role"})
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    // ✅ CRITICAL: Same here
    @JsonIgnoreProperties({"enrollments", "submissions", "coursesCreated", "password", "role"})
    private User receiver;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime sentAt;
}   