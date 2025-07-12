package com.example.btportal.repository;

import com.example.btportal.model.EnrollingTrainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollingTraineeRepository extends JpaRepository<EnrollingTrainee, Long> {
    EnrollingTrainee findByPostApplication_Email(String email);
}
