package com.example.btportal.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostApplicationResponse {
    private Long id;
    private Long generatePostApplicationId;
    private String surname;
    private String fullnames;
    private String gender;
    private Integer age;
    private String race;
    private String email;
    private String phoneNumber;
    private Date createdDate;
    // List of file names or IDs from FileDocument
    private List<String> fileNames;
    // Optional: if trainee exists, show its ID
    private Long enrollingTraineeId;
}
