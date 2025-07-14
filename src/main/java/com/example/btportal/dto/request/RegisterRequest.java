package com.example.btportal.dto.request;


import com.example.btportal.model.Role;

/**
 * Data Transfer Object (DTO) for registration requests.
 * Used to map incoming JSON request body to Java object.
 */
// @Getter @Setter // If using Lombok
public class RegisterRequest {
    public String firstname;
    public String surname;
    public String contactNumber;
    public String password;
    public String email;
    // Role can be optional; if not provided, default to STUDENT in service layer
    public Role role;
}
