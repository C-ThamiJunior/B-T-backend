package com.example.btportal.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private ContactDetails contactDetails;

    private Long assignedTo;
    private Long assignedBy;
    private String contactDate;
    private String meetingDate;

    @Enumerated(EnumType.STRING)
    private CompanyStatus status;

    private Long escalatedTo;
    @Column(length = 1000)
    private String notes;
    private String createdAt;

    // Enum for company status
    public enum CompanyStatus {
        PENDING, CLOSED, ESCALATED
    }
}
