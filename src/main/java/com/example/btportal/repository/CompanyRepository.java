package com.example.btportal.repository;

import com.example.btportal.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> { // Company ID type is Long
    List<Company> findByAssignedTo(Long userId); // Changed parameter type to Long
    List<Company> findByStatus(Company.CompanyStatus status);
    boolean existsByNameIgnoreCase(String name);
    Company findByNameIgnoreCase(String name);
}