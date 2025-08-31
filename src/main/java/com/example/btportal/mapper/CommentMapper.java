package com.example.btportal.mapper;

import com.example.btportal.dto.CommentDTO;
import com.example.btportal.model.Comment;

public class CommentMapper {

    public static CommentDTO toDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getFirstname(), // assuming User has getUsername()
                comment.getCompany().getId(),
                comment.getCreatedAt()
        );
    }
}
