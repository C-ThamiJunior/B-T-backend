package com.example.btportal.repository;

import com.example.btportal.model.FileDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileDocumentRepository extends JpaRepository<FileDocument, Long> {
    // Optional: findByFileName(String fileName), etc.\
    List<FileDocument> findByFileName(String fileName);
}
