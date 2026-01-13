package com.example.btportal.repository;

import com.example.btportal.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    // ✅ Fetch modules directly by course ID at the database level
    List<Module> findByCourseId(Long courseId);
}
