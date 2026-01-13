package com.example.btportal.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createAt;
    private Long facilitatorId;
    private String facilitatorName;
}
