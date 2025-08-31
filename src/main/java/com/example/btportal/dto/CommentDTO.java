package com.example.btportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private String username;  // we only expose the username, not full User entity
    private Long companyId;   // reference to the company
    private LocalDateTime createdAt;
}
