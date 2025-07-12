package com.example.btportal.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostApplicationRequest {
    private Long generatePostApplicationId;

    private String surname;
    private String fullnames;
    private String gender;
    private Integer age;
    private String race;
    private String email;
    private String phoneNumber;
    private String password;

    // File uploads from the client
    private List<MultipartFile> files;
}


