/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.gomoku_demo.service;

import com.example.gomoku_demo.dto.ChatMessageDTO;
import com.example.gomoku_demo.dto.UserDTO;
import com.example.gomoku_demo.model.ChatMessage;
import com.example.gomoku_demo.repository.ChatRepository;
import com.example.gomoku_demo.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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

    @Autowired
    private ModelMapper modelMapper;

    public List<ChatMessageDTO> getPrivateMessages(Long senderId, Long receiverId) {
        List<ChatMessage> chatMessages = chatRepository.getPrivateMessages(senderId, receiverId);
        chatRepository.updateHasRead(senderId, receiverId);

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<ChatMessageDTO> chatMessageDTOs
                = chatMessages
                        .stream()
                        .map(source -> modelMapper.map(source, ChatMessageDTO.class))
                        .collect(Collectors.toList());
        return chatMessageDTOs;
    }

}
