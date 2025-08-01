package com.example.btportal.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime sentAt;
}
