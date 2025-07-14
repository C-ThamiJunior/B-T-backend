package com.example.btportal.controller;


import com.example.btportal.dto.request.GeneratePostApplicationRequest;
import com.example.btportal.dto.request.PostApplicationRequest;
import com.example.btportal.dto.response.GeneratePostApplicationResponse;
import com.example.btportal.dto.response.PostApplicationResponse;
import com.example.btportal.model.*;
import com.example.btportal.repository.GeneratePostApplicationRepository;
import com.example.btportal.security.JwtUtil;
import com.example.btportal.service.EnrollingTrainieeService;
import com.example.btportal.service.GeneratePostApplicationService;
import com.example.btportal.service.PostApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ApplicationController {

    @Autowired
    private GeneratePostApplicationRepository postRepository;
    private final GeneratePostApplicationService generatePostApplicationService;
    private final JwtUtil jwtUtil;
    private final PostApplicationService postApplicationService;
    private final EnrollingTrainieeService enrollingTrainieeService;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;


    public ApplicationController(GeneratePostApplicationService generatePostApplicationService, PostApplicationService postApplicationService, EnrollingTrainieeService enrollingTrainieeService, JwtUtil jwtUtil, ObjectMapper objectMapper, PasswordEncoder passwordEncoder) {
        this.generatePostApplicationService = generatePostApplicationService;
        this.postApplicationService = postApplicationService;
        this.enrollingTrainieeService = enrollingTrainieeService;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/post/createpostform")
    public String generatePostApplication(@RequestBody GeneratePostApplicationRequest generatePostApplicationRequest) {

        generatePostApplicationService.saveGeneratePostApplication(generatePostApplicationRequest);

        return "You have successfully created a job/intern/learnership post";
    }

//    @GetMapping("/post/getcreatedallposts")
//    public List<GeneratePostApplicationResponse> getAllGeneratePostApplication() {
//        List<GeneratePostApplicationResponse> generatedPosts = new ArrayList<>();
//
//        generatedPosts = generatePostApplicationService.getGeneratedPostAll();
//
//        return generatedPosts;
//
//    }

    @GetMapping("/post/visible")
    public List<GeneratePostApplicationResponse> getVisiblePosts() {
        return generatePostApplicationService.getVisiblePosts();
    }


    @PostMapping(value = "/post/apply", consumes = "multipart/form-data")
    public String uploadPostApplication(@ModelAttribute PostApplicationRequest request) {
        try {
            // 1. Create and populate the PostApplication entity
            PostApplication postApp = new PostApplication();
            postApp.setSurname(request.getSurname());
            postApp.setFullnames(request.getFullnames());
            postApp.setGender(request.getGender());
            postApp.setAge(request.getAge());
            postApp.setRace(request.getRace());
            postApp.setEmail(request.getEmail());
            postApp.setPhoneNumber(request.getPhoneNumber());
            postApp.setPassword(passwordEncoder.encode(request.getPassword()));
            postApp.setCreatedDate(new Date());

            // 2. Link GeneratePostApplication by ID
            GeneratePostApplication gpa = new GeneratePostApplication();
            gpa.setId(request.getGeneratePostApplicationId());
            postApp.setGeneratePostApplication(gpa);

            // 3. Convert MultipartFile list into FileDocument entities
            List<FileDocument> fileDocs = new ArrayList<>();
            if (request.getFiles() != null) {
                for (MultipartFile file : request.getFiles()) {
                    FileDocument fileDoc = new FileDocument();
                    fileDoc.setFileName(file.getOriginalFilename());
                    fileDoc.setFileType(file.getContentType());
                    fileDoc.setData(file.getBytes());
                    fileDoc.setPostApplication(postApp);
                    fileDocs.add(fileDoc);
                }
            }

            postApp.setFiles(fileDocs);

            // 4. Save to database via service
           String response = postApplicationService.savePostApplication(postApp);

            return response;

        } catch (IOException e) {
            return "File processing error: " + e.getMessage();
        } catch (Exception e) {
            return "Failed to save application: " + e.getMessage();
        }
    }

    @GetMapping("/post/all-applications")
    public List<PostApplicationResponse> getAllApplications() {
        return postApplicationService.getAllApplications();
    }


    @GetMapping("/post/getpostapplication/{id}")
    public PostApplicationResponse getPostApplication(@PathVariable Long id) {
        PostApplicationResponse response;

        response = postApplicationService.getPostApplicationById(id);

        return response;
    }

    @PutMapping("/post/update/{id}")
    public ResponseEntity<GeneratePostApplicationResponse> updatePost(
            @PathVariable Long id,
            @RequestBody GeneratePostApplicationRequest request
    ) {
        GeneratePostApplicationResponse updated = generatePostApplicationService.updateGeneratePostApplication(id, request);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/hide/{id}")
    public ResponseEntity<?> hidePost(@PathVariable Long id) {
        Optional<GeneratePostApplication> post = postRepository.findById(id);
        if (post.isPresent()) {
            GeneratePostApplication existingPost = post.get();
            existingPost.setHidden(true);
            postRepository.save(existingPost);
            return ResponseEntity.ok("Post hidden successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found.");
    }

}
