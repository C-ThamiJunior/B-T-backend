package com.example.btportal.service;

import com.example.btportal.exception.DuplicateCompanyException;
import com.example.btportal.exception.ResourceNotFoundException;
import com.example.btportal.model.Company;
import com.example.btportal.model.User;
import com.example.btportal.repository.CompanyRepository;
import com.example.btportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

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

    public Company patchCompany(Long id, Map<String, Object> updates) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (updates.containsKey("status")) {
            company.setStatus(Company.CompanyStatus.valueOf(updates.get("status").toString().toUpperCase()));
        }
        if (updates.containsKey("notes")) {
            company.setNotes(updates.get("notes").toString());
        }
        // Add more fields as needed...

        return companyRepository.save(company);
    }


    public Company updateCompany(Long id, Company companyDetails) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));

        if (companyDetails.getStatus() != null) {
            company.setStatus(companyDetails.getStatus());
        }
        if (companyDetails.getMeetingDate() != null) {
            company.setMeetingDate(companyDetails.getMeetingDate());
        }
        // Only update escalatedTo if it's provided and not null, and validate if status is escalated
        if (companyDetails.getEscalatedTo() != null) {
            company.setEscalatedTo(companyDetails.getEscalatedTo());
        }
        if (company.getStatus() == Company.CompanyStatus.ESCALATED && company.getEscalatedTo() != null) {
            userRepository.findById(company.getEscalatedTo())
                    .orElseThrow(() -> new ResourceNotFoundException("Escalated to user not found with ID: " + company.getEscalatedTo()));
        }
        if (companyDetails.getNotes() != null) {
            company.setNotes(companyDetails.getNotes());
        }

        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));
        companyRepository.delete(company);
    }

    public List<Company> getCompaniesByAssignedTo(Long userId) { // Changed parameter type to Long
        return companyRepository.findByAssignedTo(userId);
    }

    public List<Company> getCompaniesByStatus(Company.CompanyStatus status) {
        return companyRepository.findByStatus(status);
    }
}
