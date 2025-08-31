package com.example.btportal.controller;

import com.example.btportal.dto.CommentDTO;
import com.example.btportal.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public CommentDTO addComment(@RequestParam Long companyId,
                                 @RequestParam Long userId,
                                 @RequestParam String content) {
        return commentService.addComment(companyId, userId, content);
    }

    @GetMapping("/company/{companyId}")
    public List<CommentDTO> getCommentsByCompany(@PathVariable Long companyId) {
        return commentService.getCommentsByCompany(companyId);
    }
}
