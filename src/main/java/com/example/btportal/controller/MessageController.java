package com.example.btportal.controller;

import com.example.btportal.model.Message;
import com.example.btportal.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // Send a new message
    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        // The request body should be:
        // { "sender": { "id": 1 }, "receiver": { "id": 2 }, "content": "Hello!" }
        return ResponseEntity.ok(messageService.sendMessage(message));
    }

    // Get chat history between two users
    @GetMapping("/conversation/{userId1}/{userId2}")
    public ResponseEntity<List<Message>> getConversation(
            @PathVariable Long userId1,
            @PathVariable Long userId2) {
        return ResponseEntity.ok(messageService.getConversation(userId1, userId2));
    }
}