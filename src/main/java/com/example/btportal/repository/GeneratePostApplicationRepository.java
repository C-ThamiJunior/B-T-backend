package com.example.btportal.repository;


import com.example.btportal.model.GeneratePostApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneratePostApplicationRepository extends JpaRepository<GeneratePostApplication, Long> {
    @Query("SELECT p FROM GeneratePostApplication p WHERE p.hidden = false")
    List<GeneratePostApplication> findVisiblePosts();
    List<GeneratePostApplication> findByHiddenFalse();

}

