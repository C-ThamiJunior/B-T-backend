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

    // ✅ FIX: Use Map<String, Object> to stop the 500 error caused by "message" field
    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> payload) {
        try {
            // 1. Extract IDs safely (Handles both Integer and String inputs)
            Object senderIdObj = ((Map)payload.get("sender")).get("id");
            Object receiverIdObj = ((Map)payload.get("receiver")).get("id");

            // 2. Extract Content (Backend expects 'content', ignores 'message')
            String content = (String) payload.get("content");

            Long senderId = Long.valueOf(senderIdObj.toString());
            Long receiverId = Long.valueOf(receiverIdObj.toString());

            // 3. Find Users
            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
            User receiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

            // 4. Create and Save
            Message message = new Message();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setContent(content);
            message.setSentAt(LocalDateTime.now());

            return ResponseEntity.ok(messageService.sendMessage(message));

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