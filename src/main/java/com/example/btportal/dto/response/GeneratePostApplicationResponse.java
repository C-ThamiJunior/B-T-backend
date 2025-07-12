package com.example.btportal.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class GeneratePostApplicationResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Date closingDate;
    private String postType;
    private Long postApplicationId; // optional reference to PostApplication if it exists
}
