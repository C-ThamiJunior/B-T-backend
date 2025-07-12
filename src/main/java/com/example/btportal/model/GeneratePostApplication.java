package com.example.btportal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
public class GeneratePostApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String location;
    private Date closingDate;
    private String postType;
    @OneToMany(mappedBy = "generatePostApplication")
    private List<PostApplication> postApplications;
    @Column(name = "hidden")
    private Boolean hidden = false;
}
