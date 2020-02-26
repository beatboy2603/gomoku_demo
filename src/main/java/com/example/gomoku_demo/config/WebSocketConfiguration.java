/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.gomoku_demo.config;

import com.example.gomoku_demo.controller.ChatController;
import com.example.gomoku_demo.model.ChatMessage;
import com.example.gomoku_demo.model.CustomUserDetail;
import com.example.gomoku_demo.repository.UserRepository;
import java.util.HashMap;
import javax.servlet.http.HttpSessionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 *
 * @author ADMIN
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chatDemo", "/secured/room").withSockJS();
//        registry.addEndpoint("/secured/room").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/secured/user/queue/specific-user");
        registry.setApplicationDestinationPrefixes("/api/chat");

//        registry.enableSimpleBroker("/secured/user/queue/specific-user");
//        registry.setApplicationDestinationPrefixes("/api/p2p-chat");
        registry.setUserDestinationPrefix("/secured/user");
    }

    @EventListener(SessionConnectedEvent.class)
    public void handleWebsocketConnectedListner(SessionConnectedEvent event) {
    }

    @EventListener(SessionDisconnectEvent.class)
    public void handleWebsocketDisconnectListner(SessionDisconnectEvent event) {
//        String username = SimpMessageHeaderAccessor.getSessionAttributes(event.getMessage().getHeaders())
//                .get("username").toString();
//        ChatMessage chatMessage = new ChatMessage();
//        userRepository.findById(((CustomUserDetail) authentication.getPrincipal()).getId())
//                .ifPresent(u -> {
//                    chatMessage.setSender(u);
//                    chatMessage.setContent(u.getUsername() + " has left the room!");
//                    chatMessage.setMessageType(ChatMessage.MessageType.LEAVE);
//                    this.template.convertAndSend("/topic/public", chatMessage);
//                });
    }
}
