package com.example.btportal.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String firstName;
    private String surname;
    private String email;
    private String phone;
    private String password;
}
