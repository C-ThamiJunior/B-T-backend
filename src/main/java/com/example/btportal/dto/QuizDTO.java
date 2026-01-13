// B-T-backend/src/main/java/com/example/btportal/dto/QuizDTO.java
package com.example.btportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {
    private Long id;
    private String title;
    private String description;
    private boolean timed;
    private int timeLimitInMinutes;
    private int totalMarks; // ✅ Added this field

    private Long lessonId;
    private Long moduleId;
    private Long courseId;

    private List<QuestionDTO> questions;
}