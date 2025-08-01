package com.example.btportal.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDetails {
    private String email;
    private String phone;
    private String address;
    private String contactPerson;
}
