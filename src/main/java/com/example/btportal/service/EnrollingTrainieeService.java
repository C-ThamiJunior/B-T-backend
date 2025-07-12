package com.example.btportal.service;

import com.example.btportal.dto.request.EnrollingTraineeRequest;
import com.example.btportal.dto.response.EnrollingTraineeResponse;
import com.example.btportal.model.EnrollingTrainee;
import com.example.btportal.model.PostApplication;
import com.example.btportal.repository.EnrollingTraineeRepository;
import com.example.btportal.repository.PostApplicationRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnrollingTrainieeService {

    private final EnrollingTraineeRepository enrollingTraineeRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostApplicationRepository postApplicationRepository;

    public EnrollingTrainieeService(EnrollingTraineeRepository enrollingTraineeRepository, PasswordEncoder passwordEncoder, PostApplicationRepository postApplicationRepository){
        this.enrollingTraineeRepository = enrollingTraineeRepository;
        this.passwordEncoder = passwordEncoder;
        this.postApplicationRepository = postApplicationRepository;
    }

    public String authenticateEnrollingTraniee(String email, String rawPassword){
        EnrollingTrainee enrollingTrainee = enrollingTraineeRepository.findByPostApplication_Email(email);

        if(enrollingTrainee==null){
            return "Your username is INVALID";
        }

        if(passwordEncoder.matches(rawPassword, enrollingTrainee.getPostApplication().getPassword())){
            return "You have successfully Logged In";
        }

        return "You have entered wrong password";
    }

    public EnrollingTraineeResponse getEnrollingTraineeById(Long id) {
        Optional<EnrollingTrainee> optionalTrainee = enrollingTraineeRepository.findById(id);

        if (optionalTrainee.isPresent()) {
            EnrollingTrainee trainee = optionalTrainee.get();

            // Convert to DTO
            EnrollingTraineeResponse dto = new EnrollingTraineeResponse();
            dto.setId(trainee.getId());
            dto.setPostApplicationId(trainee.getPostApplication().getId());

            return dto;
        } else {
            throw new RuntimeException("Trainee not found with id: " + id);
        }
    }

    public String createTrainee(EnrollingTraineeRequest request) {
        PostApplication post = postApplicationRepository.findById(request.getPostApplicationId())
                .orElseThrow(() -> new RuntimeException("Applicant is not found"));

        EnrollingTrainee trainee = new EnrollingTrainee();
        trainee.setPostApplication(post);
        enrollingTraineeRepository.save(trainee);

        return "The trainee is successfully registered";
    }
}
