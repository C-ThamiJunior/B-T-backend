package com.example.btportal.service;

import com.example.btportal.dto.CommentDTO;
import com.example.btportal.model.Comment;
import com.example.btportal.model.Company;
import com.example.btportal.model.User;
import com.example.btportal.repository.CommentRepository;
import com.example.btportal.repository.CompanyRepository;
import com.example.btportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
