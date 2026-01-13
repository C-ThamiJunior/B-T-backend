package com.example.btportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleDTO {
    private Long id;
    private String title;
    private String description;
    private Integer orderIndex;
    private Long courseId; // reference to parent Course
}
