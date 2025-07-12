package com.example.btportal.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class EnrollingTrainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn
    private PostApplication postApplication;
}
