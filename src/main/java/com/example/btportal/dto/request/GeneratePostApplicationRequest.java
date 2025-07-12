package com.example.btportal.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GeneratePostApplicationRequest {
    private String title;
    private String description;
    private String location;
    private Date closingDate;
    private String postType;

}
