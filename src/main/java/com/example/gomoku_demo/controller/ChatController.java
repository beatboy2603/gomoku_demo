/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.gomoku_demo.controller;

import com.example.gomoku_demo.config.WebSocketConfiguration;
import com.example.gomoku_demo.dto.ChatMessageDTO;
import com.example.gomoku_demo.dto.UserDTO;
import com.example.gomoku_demo.model.ChatMessage;
import com.example.gomoku_demo.model.CustomUserDetail;
import com.example.gomoku_demo.model.User;
import com.example.gomoku_demo.repository.ChatRepository;
import com.example.gomoku_demo.repository.UserRepository;
import com.example.gomoku_demo.service.ChatService;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;

/**
 *
 * @author ADMIN
 */
@Controller

public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    @GetMapping("/chat")
    public String goToChat(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        Long userId = ((CustomUserDetail) authentication.getPrincipal()).getId();
        model.addAttribute("userId", userId);
        model.addAttribute("friendList", userRepository.findAll()
                .stream()
                .filter(u -> !u.getId().equals(userId))
                .map(source -> modelMapper.map(source, UserDTO.class))
                .collect(Collectors.toList()));
        model.addAttribute("friendsWithNewMessages", userRepository.getUsersWithNewMessages(userId)
                .stream()
                .map(source -> modelMapper.map(source, UserDTO.class))
                .collect(Collectors.toList()));
        return "chat";
    }

    @PostMapping("/chat/registerName")
    public String registerName(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "fragments/chatRoom :: publicRoom";
    }

    @MessageMapping("register")
    @SendTo("/topic/public")
    public ChatMessageDTO register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor,
            Authentication authentication) {
        User newUser = userRepository.findUserById(chatMessage.getSender().getId());
        if (ChatRestController.connectedUsers.contains(newUser)) {
            return null;
        } else {
            headerAccessor.getSessionAttributes().put("username", authentication.getName());
            chatMessage.setTime(new Date());
            chatMessage.setSender(newUser);
            ChatRestController.connectedUsers.add(newUser);
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
            return modelMapper.map(chatMessage, ChatMessageDTO.class);
        }
    }

    @MessageMapping("send")
    @SendTo("/topic/public")
    public ChatMessageDTO sendPublicMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setTime(new Date());
//        chatMessage = chatRepository.saveAndFlush(chatMessage);
        chatMessage.setSender(userRepository.findUserById(chatMessage.getSender().getId()));
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(chatMessage, ChatMessageDTO.class);
    }

    @MessageMapping("/secured/room-{userId}")
    @SendTo("/secured/user/queue/specific-user-user{userId}")
    public ChatMessageDTO sendSpecific(
            @DestinationVariable String userId,
            @Payload ChatMessage chatMessage,
            Principal user,
            @Header("simpSessionId") String sessionId) throws Exception {
        chatMessage.setTime(new Date());
        chatMessage = chatRepository.saveAndFlush(chatMessage);
        chatMessage.setSender(userRepository.findUserById(chatMessage.getSender().getId()));
        chatMessage.setReceiver(userRepository.findUserById(chatMessage.getReceiver().getId()));
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(chatMessage, ChatMessageDTO.class);
    }
}
