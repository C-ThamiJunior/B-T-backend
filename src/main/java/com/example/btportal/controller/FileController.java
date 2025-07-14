package com.example.btportal.controller;

import com.example.btportal.model.FileDocument;
import com.example.btportal.repository.FileDocumentRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "*")
public class FileController {

    private final FileDocumentRepository fileDocumentRepository;

    public FileController(FileDocumentRepository fileDocumentRepository) {
        this.fileDocumentRepository = fileDocumentRepository;
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        List<FileDocument> files = fileDocumentRepository.findByFileName(fileName);

        if (files.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        FileDocument file = files.get(0);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .body(file.getData());
    }
}
