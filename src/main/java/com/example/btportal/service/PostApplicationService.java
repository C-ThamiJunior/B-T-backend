package com.example.btportal.service;

import com.example.btportal.dto.response.PostApplicationResponse;
import com.example.btportal.model.PostApplication;
import com.example.btportal.repository.PostApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostApplicationService {

    private final PostApplicationRepository postApplicationRepository;
    private final EmailService mailService;

    // ✅ FIX: Constructor Injection for BOTH dependencies
    @Autowired
    public PostApplicationService(PostApplicationRepository postApplicationRepository, EmailService mailService) {
        this.postApplicationRepository = postApplicationRepository;
        this.mailService = mailService;
    }

    private PostApplicationResponse mapToResponse(PostApplication post) {
        PostApplicationResponse dto = new PostApplicationResponse();
        dto.setId(post.getId());
        dto.setGeneratePostApplicationId(
                post.getGeneratePostApplication() != null ? post.getGeneratePostApplication().getId() : null
        );
        dto.setSurname(post.getSurname());
        dto.setFullnames(post.getFullnames());
        dto.setGender(post.getGender());
        dto.setAge(post.getAge());
        dto.setRace(post.getRace());
        dto.setEmail(post.getEmail());
        dto.setPhoneNumber(post.getPhoneNumber());
        dto.setCreatedDate(post.getCreatedDate());

        // ✅ FIX: Null Check prevents crash if there are no files
        if (post.getFiles() != null) {
            dto.setFileNames(
                    post.getFiles().stream()
                            .map(file -> file.getFileName())
                            .collect(Collectors.toList())
            );
        } else {
            dto.setFileNames(Collections.emptyList());
        }

        dto.setEnrollingTraineeId(
                post.getEnrollingTrainee() != null ? post.getEnrollingTrainee().getId() : null
        );

        return dto;
    }

    @Transactional(readOnly = true)
    public List<PostApplicationResponse> getAllApplications() {
        List<PostApplication> apps = postApplicationRepository.findAll();
        return apps.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public String savePostApplication(PostApplication postApplication) {
        // 1. Save to Database
        postApplicationRepository.save(postApplication);

        // 2. Send Email (Wrapped in try-catch so it doesn't fail the submission if email fails)
        try {
            mailService.sendApplicationEmailWithCV(postApplication);
        } catch (Exception e) {
            System.err.println("Warning: Could not send email for application ID " + postApplication.getId() + ": " + e.getMessage());
            // We do NOT re-throw, so the user still sees "Success" even if email fails.
        }

        return "Your application is successfully submitted";
    }

    @Transactional(readOnly = true)
    public PostApplicationResponse getPostApplicationById(Long id) {
        PostApplication post = postApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PostApplication not found with id: " + id));

        return mapToResponse(post);
    }
}