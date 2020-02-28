/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.gomoku_demo.controller;

import com.example.gomoku_demo.config.WebSocketConfiguration;
import com.example.gomoku_demo.model.ChatMessage;
import com.example.gomoku_demo.model.User;
import com.example.gomoku_demo.service.ChatService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
public class ChatRestController {
    
    @Autowired
    private ChatService chatService;
    
    public static List<User> connectedUsers = new ArrayList<>();
    
    @PostMapping("/api/chat/getPrivateMessages/{senderId}/{receiverId}")
    public List<ChatMessage> getPrivateMessages(@PathVariable("senderId") Long senderId, 
            @PathVariable("receiverId") Long receiverId ){
        return chatService.getPrivateMessages(senderId, receiverId);
    }
    
    @GetMapping("/api/chat/getPublicUsers")
    public List<User> getPublicUsers(){
        return connectedUsers;
    }
    
}
