package com.example.btportal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.DocumentType;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class FileDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    @Lob
    private byte[] data;
    @ManyToOne
    @JoinColumn
    private PostApplication postApplication;
}
