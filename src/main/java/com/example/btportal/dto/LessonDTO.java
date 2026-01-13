package com.example.btportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDTO {
    private Long id;
    private String title;
    private String description; // ✅ Added
    private String url; // ✅ Added
    private String contentType;
    private Integer orderIndex;
    private Long moduleId;
}