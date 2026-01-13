package com.example.btportal.controller;

import com.example.btportal.dto.ModuleDTO;
import com.example.btportal.mapper.ModuleMapper;
import com.example.btportal.model.Module;
import com.example.btportal.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    // ✅ Get all modules
    @GetMapping
    public ResponseEntity<List<ModuleDTO>> getAllModules() {
        List<ModuleDTO> modules = moduleService.getAllModules()
                .stream()
                .map(ModuleMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(modules);
    }

    // ✅ Get module by ID
    @GetMapping("/{id}")
    public ResponseEntity<ModuleDTO> getModuleById(@PathVariable Long id) {
        return moduleService.getModuleById(id)
                .map(ModuleMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Get modules by course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ModuleDTO>> getModulesByCourse(@PathVariable Long courseId) {
        List<ModuleDTO> modules = moduleService.getModulesByCourse(courseId)
                .stream()
                .map(ModuleMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(modules);
    }

    // ✅ Create module
    @PostMapping
    public ResponseEntity<ModuleDTO> createModule(@RequestBody ModuleDTO moduleDTO) {
        Module module = ModuleMapper.toEntity(moduleDTO);
        Module saved = moduleService.createModule(module);
        ModuleDTO response = ModuleMapper.toDTO(saved);
        return ResponseEntity.created(URI.create("/api/modules/" + saved.getId())).body(response);
    }

    // ✅ Update module
    @PutMapping("/{id}")
    public ResponseEntity<ModuleDTO> updateModule(@PathVariable Long id, @RequestBody ModuleDTO moduleDTO) {
        Module updated = ModuleMapper.toEntity(moduleDTO);
        Module saved = moduleService.updateModule(id, updated);
        return ResponseEntity.ok(ModuleMapper.toDTO(saved));
    }

    // ✅ Delete module
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        moduleService.deleteModule(id);
        return ResponseEntity.noContent().build();
    }
}
