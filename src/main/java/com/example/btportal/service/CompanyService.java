package com.example.btportal.service;

import com.example.btportal.exception.DuplicateCompanyException;
import com.example.btportal.exception.ResourceNotFoundException;
import com.example.btportal.model.Company;
import com.example.btportal.model.User;
import com.example.btportal.repository.CompanyRepository;
import com.example.btportal.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    public Company createCompany(Company company) {
        Company existingCompany = companyRepository.findByNameIgnoreCase(company.getName().trim());
        if (existingCompany != null) {
            String assignedUserName = userRepository.findById(existingCompany.getAssignedTo())
                    .map(User::getFirstname)
                    .orElse("an unknown user");
            throw new DuplicateCompanyException(
                    String.format("Company \"%s\" is already assigned to %s", company.getName(), assignedUserName)
            );
        }

        // Validate assignedTo user exists
        userRepository.findById(company.getAssignedTo())
                .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found with ID: " + company.getAssignedTo()));

        // Validate escalatedTo user exists if escalated
        if (company.getStatus() == Company.CompanyStatus.ESCALATED && company.getEscalatedTo() != null) { // Check for null before empty string
            userRepository.findById(company.getEscalatedTo())
                    .orElseThrow(() -> new ResourceNotFoundException("Escalated to user not found with ID: " + company.getEscalatedTo()));
        }

        company.setCreatedAt(Instant.now().toString()); // Still storing as string to match previous frontend assumption
        return companyRepository.save(company);
    }

    public Company updateCompany(Long id, Company updatedCompany) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));

        // If updating name, check for duplicate
        if (updatedCompany.getName() != null && !updatedCompany.getName().trim().equalsIgnoreCase(existingCompany.getName())) {
            Company duplicate = companyRepository.findByNameIgnoreCase(updatedCompany.getName().trim());
            if (duplicate != null && !duplicate.getId().equals(id)) {
                String assignedUserName = userRepository.findById(duplicate.getAssignedTo())
                        .map(User::getFirstname)
                        .orElse("an unknown user");
                throw new DuplicateCompanyException(
                        String.format("Company \"%s\" is already assigned to %s", updatedCompany.getName(), assignedUserName)
                );
            }
            existingCompany.setName(updatedCompany.getName().trim());
        }

        if (updatedCompany.getContactDetails() != null) {
            existingCompany.setContactDetails(updatedCompany.getContactDetails());
        }

        if (updatedCompany.getAssignedTo() != null) {
            userRepository.findById(updatedCompany.getAssignedTo())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found with ID: " + updatedCompany.getAssignedTo()));
            existingCompany.setAssignedTo(updatedCompany.getAssignedTo());
        }

        if (updatedCompany.getAssignedBy() != null) {
            existingCompany.setAssignedBy(updatedCompany.getAssignedBy());
        }

        if (updatedCompany.getContactDate() != null) {
            existingCompany.setContactDate(updatedCompany.getContactDate());
        }

        if (updatedCompany.getMeetingDate() != null) {
            existingCompany.setMeetingDate(updatedCompany.getMeetingDate());
        }

        if (updatedCompany.getStatus() != null) {
            existingCompany.setStatus(updatedCompany.getStatus());
        }

        if (updatedCompany.getEscalatedTo() != null) {
            userRepository.findById(updatedCompany.getEscalatedTo())
                    .orElseThrow(() -> new ResourceNotFoundException("Escalated to user not found with ID: " + updatedCompany.getEscalatedTo()));
            existingCompany.setEscalatedTo(updatedCompany.getEscalatedTo());
        }

        if (updatedCompany.getNotes() != null) {
            existingCompany.setNotes(updatedCompany.getNotes());
        }

        // Never update createdAt
        return companyRepository.save(existingCompany);
    }

    @Transactional
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));

        logger.info("Deleting company with ID {} and name '{}'", company.getId(), company.getName());

        companyRepository.delete(company);

        logger.info("Company with ID {} successfully deleted", id);
    }


    public List<Company> getCompaniesByAssignedTo(Long userId) { // Changed parameter type to Long
        return companyRepository.findByAssignedTo(userId);
    }

    public List<Company> getCompaniesByStatus(Company.CompanyStatus status) {
        return companyRepository.findByStatus(status);
    }
}
