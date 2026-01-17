package com.example.btportal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter // ✅ Safer than @Data for JPA
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    // ✅ FIX: Stop the loop. Don't load the User's lists when fetching a message.
    @JsonIgnoreProperties({"enrollments", "submissions", "coursesCreated", "sentMessages", "receivedMessages", "password"})
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    // ✅ FIX: Same here.
    @JsonIgnoreProperties({"enrollments", "submissions", "coursesCreated", "sentMessages", "receivedMessages", "password"})
    private User receiver;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime sentAt;

    @PrePersist
    protected void onCreate() {
        this.sentAt = LocalDateTime.now();
    }
}