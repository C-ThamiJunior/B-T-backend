package com.example.btportal.service;

import com.example.btportal.dto.response.PostApplicationResponse;
import com.example.btportal.model.PostApplication;
import com.example.btportal.repository.PostApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PostApplicationService {

    @Autowired
    private EmailService mailService;

    private final PostApplicationRepository postApplicationRepository;
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

        dto.setFileNames(
                post.getFiles().stream()
                        .map(file -> file.getFileName())
                        .collect(Collectors.toList())
        );

        dto.setEnrollingTraineeId(
                post.getEnrollingTrainee() != null ? post.getEnrollingTrainee().getId() : null
        );

        return dto;
    }


    public PostApplicationService(PostApplicationRepository postApplicationRepository) {
        this.postApplicationRepository = postApplicationRepository;
    }

    public List<PostApplicationResponse> getAllApplications() {
        List<PostApplication> apps = postApplicationRepository.findAll();
        return apps.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public String savePostApplication(PostApplication postApplication) {
         postApplicationRepository.save(postApplication);
         mailService.sendApplicationEmailWithCV(postApplication);
        return "Your application is successfully submitted";
    }

    public PostApplicationResponse getPostApplicationById(Long id) {
        PostApplication post = postApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PostApplication not found with id: " + id));

        return mapToResponse(post);
    }

}
