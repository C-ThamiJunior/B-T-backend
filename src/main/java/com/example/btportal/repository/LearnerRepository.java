package com.example.btportal.repository;

import com.example.btportal.model.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LearnerRepository extends JpaRepository<Learner, String> {
    Optional<Learner> findByEmail(String email);
    boolean existsByEmail(String email);
//    Optional<Learner> authenticateUser(String username, String password);
    boolean existsByPhone(String phone);
}