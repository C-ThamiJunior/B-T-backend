package com.example.btportal.service;

import com.example.btportal.dto.CommentDTO;
import com.example.btportal.exception.ResourceNotFoundException;
import com.example.btportal.model.Comment;
import com.example.btportal.model.Company;
import com.example.btportal.model.User;
import com.example.btportal.model.Role;
import com.example.btportal.repository.CommentRepository;
import com.example.btportal.repository.CompanyRepository;
import com.example.btportal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    public CommentDTO addComment(Long companyId, Long userId, String content) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCompany(company);
        comment.setUser(user);

        Comment saved = commentRepository.save(comment);

        return toDTO(saved);
    }

    public void deleteComment(Long commentId, Long requesterId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId));

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + requesterId));

        boolean isAuthor = comment.getUser().getId().equals(requester.getId());
        boolean isAdmin = requester.getRole() == Role.ADMIN;

        if (!isAuthor && !isAdmin) {
            throw new RuntimeException("You do not have permission to delete this comment");
        }

        commentRepository.delete(comment);
        log.info("Deleted comment with id: {} by user: {}", commentId, requester.getId());
    }

    public List<CommentDTO> getCommentsByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return commentRepository.findByCompany(company)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private CommentDTO toDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUsername(comment.getUser().getFirstname() + " " + comment.getUser().getSurname());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}
