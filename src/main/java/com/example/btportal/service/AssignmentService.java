package com.example.btportal.service;

import com.example.btportal.model.Assignment;
import com.example.btportal.model.FileDocument;
import com.example.btportal.repository.AssignmentRepository;
import com.example.btportal.repository.FileDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final FileDocumentRepository fileDocumentRepository; // ✅ Inject this

    // ✅ UPDATED: Now accepts a file
    public Assignment createAssignment(Assignment assignment, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            // 1. Generate unique filename to prevent overwrites
            String originalName = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalName;

            // 2. Save File to DB (Reusing your FileDocument entity)
            FileDocument fileDoc = new FileDocument();
            fileDoc.setFileName(uniqueFileName);
            fileDoc.setFileType(file.getContentType());
            fileDoc.setData(file.getBytes());
            fileDocumentRepository.save(fileDoc);

            // 3. Generate Download URL
            // This creates a URL like: http://localhost:8080/files/{uniqueFileName}
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/files/")
                    .path(uniqueFileName)
                    .toUriString();

            assignment.setFileUrl(fileDownloadUri);
        }

        return assignmentRepository.save(assignment);
    }

    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public List<Assignment> getAssignmentsByModuleId(Long moduleId) {
        return assignmentRepository.findByModuleId(moduleId);
    }

    public void deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new RuntimeException("Assignment not found with id: " + id);
        }
        assignmentRepository.deleteById(id);
    }
}