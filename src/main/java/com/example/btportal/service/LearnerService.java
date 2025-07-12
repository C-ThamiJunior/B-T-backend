package com.example.btportal.service;

import com.example.btportal.model.Learner;
import com.example.btportal.repository.LearnerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LearnerService {
    private final LearnerRepository learnerRepository;
    private final PasswordEncoder passwordEncoder;

    public LearnerService(LearnerRepository learnerRepository, PasswordEncoder passwordEncoder){
        this.learnerRepository = learnerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Learner> getAllUsers(){
        return learnerRepository.findAll();
    }

    @Transactional
    public Learner registerLearner(Learner learner){
        if(learnerRepository.existsByEmail(learner.getEmail())){
            throw new RuntimeException("Email already registered");
        }
        if (learnerRepository.existsByPhone(learner.getPhone())) {
            throw new RuntimeException("Phone number already registered");
        }
        learner.setPassword(passwordEncoder.encode(learner.getPassword()));
        return learnerRepository.save(learner);
    }

    public Optional<Learner> authenticateLearner(String email, String rawPassword){
        Optional<Learner> learnerOptional = learnerRepository.findByEmail(email);
        if(learnerOptional.isPresent()){
            Learner learner = learnerOptional.get();
            if(passwordEncoder.matches(rawPassword, learner.getPassword())){
                return Optional.of(learner);
            }
        }
        return Optional.empty();
    }

}
