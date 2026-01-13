package com.example.btportal.service;

import com.example.btportal.exception.ResourceNotFoundException;
import com.example.btportal.model.Message;
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
    private final UserRepository userRepository; // To set sender/receiver

    public Message sendMessage(Message message) {
        // Ensure sender and receiver are valid users
        userRepository.findById(message.getSender().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        userRepository.findById(message.getReceiver().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        message.setSentAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    // Gets the chat history between two users
    public List<Message> getConversation(Long userId1, Long userId2) {
        // Add findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderBySentAtAsc
        // to your MessageRepository
        return messageRepository.findConversation(userId1, userId2);
    }
}