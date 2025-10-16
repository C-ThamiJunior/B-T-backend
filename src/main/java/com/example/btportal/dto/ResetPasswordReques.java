package com.example.btportal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordReques {
     private String email;
     private String newPassword;
}
