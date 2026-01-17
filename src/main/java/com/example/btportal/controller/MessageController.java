package com.example.btportal.controller;

import com.example.btportal.model.Message;
import com.example.btportal.model.User;
import com.example.btportal.service.MessageService;
import com.example.btportal.repository.UserRepository; // Import this
import com.example.btportal.exception.ResourceNotFoundException; // Import this
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
    private final UserRepository userRepository; // Inject UserRepository directly for manual lookups

    // ✅ FIX: Accept a Map (JSON) instead of the Entity to prevent mapping errors
    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> payload) {
        try {
            // 1. Extract IDs and Content safely
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

            // 3. Create and Save Message
            Message message = new Message();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setContent(content);
            message.setSentAt(LocalDateTime.now());

            return ResponseEntity.ok(messageService.sendMessage(message));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error sending message: " + e.getMessage());
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