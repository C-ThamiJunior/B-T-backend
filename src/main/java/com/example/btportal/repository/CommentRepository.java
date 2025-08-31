package com.example.btportal.repository;

import com.example.btportal.model.Comment;
import com.example.btportal.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Find all comments for a specific company
    List<Comment> findByCompany(Company company);

    // Find all comments made by a specific user
    List<Comment> findByUserId(Long userId);
}
