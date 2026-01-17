package com.example.btportal.service;

import com.example.btportal.exception.ResourceNotFoundException;
import com.example.btportal.model.Message;
import com.example.btportal.model.User;
import com.example.btportal.repository.MessageRepository;
import com.example.btportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public Message sendMessage(Message message) {
        // Validation
        if (message.getSender() == null || message.getReceiver() == null) {
            throw new IllegalArgumentException("Sender and Receiver are required");
        }

        User sender = userRepository.findById(message.getSender().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User receiver = userRepository.findById(message.getReceiver().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSentAt(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public List<Message> getConversation(Long userId1, Long userId2) {
        return messageRepository.findConversation(userId1, userId2);
    }

    // ✅ NEW: Get all messages for the dashboard
    public List<Message> getMessagesForUser(Long userId) {
        return messageRepository.findAllByUserId(userId);
    }
}