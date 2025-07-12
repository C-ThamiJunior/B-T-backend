package com.example.btportal.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "User")
public class Learner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long learnerID;

    @Column(nullable =false)
    private String firstName;

    @Column(nullable =false)
    private String surname;

    @Column(nullable =false, unique = true)
    private String email;

    @Column(nullable =false, unique = true)
    private String phone;


    @Column(nullable = false)
    private String password;
    private Date creationDate;

    @PrePersist
    protected void onCreate() {
        this.creationDate = new Date();
    }
}
