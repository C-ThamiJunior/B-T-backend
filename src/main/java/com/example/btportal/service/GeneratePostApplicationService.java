package com.example.btportal.service;

import com.example.btportal.dto.request.GeneratePostApplicationRequest;
import com.example.btportal.dto.response.GeneratePostApplicationResponse;
import com.example.btportal.model.GeneratePostApplication;
import com.example.btportal.repository.GeneratePostApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GeneratePostApplicationService {


    private final GeneratePostApplicationRepository repository;

    public GeneratePostApplicationService(GeneratePostApplicationRepository repository) {
        this.repository = repository;
    }

    public GeneratePostApplication saveGeneratePostApplication(GeneratePostApplicationRequest dto) {
        GeneratePostApplication post = new GeneratePostApplication();
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setLocation(dto.getLocation());
        post.setClosingDate(dto.getClosingDate());
        post.setPostType(dto.getPostType());
        return repository.save(post);
    }

    public GeneratePostApplicationResponse getById(Long id) {
        GeneratePostApplication entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("GeneratePostApplication not found with id: " + id));

        return mapToDto(entity);
    }

    //Get all GeneratePostApplications as DTOs
    public List<GeneratePostApplicationResponse> getGeneratedPostAll() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ✅ Helper method for mapping
    private GeneratePostApplicationResponse mapToDto(GeneratePostApplication entity) {
        GeneratePostApplicationResponse dto = new GeneratePostApplicationResponse();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setLocation(entity.getLocation());
        dto.setClosingDate(entity.getClosingDate());
        dto.setPostType(entity.getPostType());

        if (entity.getPostApplications() != null && !entity.getPostApplications().isEmpty()) {
            dto.setPostApplicationId(entity.getPostApplications().get(0).getId());
        }

        return dto;
    }
    public GeneratePostApplicationResponse updateGeneratePostApplication(Long id, GeneratePostApplicationRequest dto) {
        GeneratePostApplication existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setLocation(dto.getLocation());
        existing.setClosingDate(dto.getClosingDate());
        existing.setPostType(dto.getPostType());

        GeneratePostApplication updated = repository.save(existing);
        return mapToDto(updated);
    }
    public List<GeneratePostApplicationResponse> getVisiblePosts() {
        return repository.findByHiddenFalse().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    public void deleteGeneratePostApplication(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Post not found with id: " + id);
        }
        repository.deleteById(id);
    }


}
