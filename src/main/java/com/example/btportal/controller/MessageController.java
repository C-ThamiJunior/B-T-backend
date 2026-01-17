package com.example.btportal.controller;

import com.example.btportal.model.Message;
import com.example.btportal.model.User;
import com.example.btportal.service.MessageService;
import com.example.btportal.repository.UserRepository;
import com.example.btportal.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> payload) {
        try {
            // 1. Manually extract only the fields we need
            // This ignores the "message" field that is causing the crash
            Object senderIdObj = ((Map)payload.get("sender")).get("id");
            Object receiverIdObj = ((Map)payload.get("receiver")).get("id");
            String content = (String) payload.get("content");

            Long senderId = Long.valueOf(senderIdObj.toString());
            Long receiverId = Long.valueOf(receiverIdObj.toString());

            // 2. Find Users
            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
            User receiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

            // 3. Save Message
            Message messageEntity = new Message();
            messageEntity.setSender(sender);
            messageEntity.setReceiver(receiver);
            messageEntity.setContent(content);
            messageEntity.setSentAt(LocalDateTime.now());

            return ResponseEntity.ok(messageService.sendMessage(messageEntity));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Message>> getUserMessages(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getMessagesForUser(userId));
    }

    @GetMapping("/conversation/{userId1}/{userId2}")
    public ResponseEntity<List<Message>> getConversation(
            @PathVariable Long userId1,
            @PathVariable Long userId2) {
        return ResponseEntity.ok(messageService.getConversation(userId1, userId2));
    }
}