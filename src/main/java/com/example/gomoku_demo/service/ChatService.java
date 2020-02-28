/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.gomoku_demo.service;

import com.example.gomoku_demo.model.ChatMessage;
import com.example.gomoku_demo.repository.ChatRepository;
import com.example.gomoku_demo.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class ChatService {
    
    @Autowired
    private ChatRepository chatRepository;
    
    @Autowired
    private UserRepository userRepository;

    public List<ChatMessage> getPrivateMessages(Long senderId, Long receiverId) {
        List<ChatMessage> chatMessages = chatRepository.getPrivateMessages(senderId, receiverId);
        chatRepository.updateHasRead(senderId, receiverId);
        return chatMessages;
    }
    
}
