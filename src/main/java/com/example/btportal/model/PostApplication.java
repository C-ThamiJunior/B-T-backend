package com.example.btportal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class PostApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "generate_post_application_id")
    private GeneratePostApplication generatePostApplication;
    private String surname;
    private String fullnames;
    private String gender;
    private Integer age;
    private String race;
    private String email;
    private String phoneNumber;
    private String password;
    private Date createdDate;
    @OneToMany(mappedBy = "postApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileDocument> files;
    @OneToOne(mappedBy = "postApplication")
    private EnrollingTrainee enrollingTrainee;

    @PrePersist
    protected void onCreate() {
        createdDate = new Date();
    }
}
