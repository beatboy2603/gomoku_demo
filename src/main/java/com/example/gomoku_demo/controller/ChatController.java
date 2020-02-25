/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.gomoku_demo.controller;

import com.example.gomoku_demo.model.ChatMessage;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ADMIN
 */
@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/chat")
    public String goToChat() {
        return "chat";
    }

    @PostMapping("/chat/registerName")
    public String registerName(@RequestBody HashMap<String, String> json, Model model) {
        model.addAttribute("username", json.get("username"));
        return "fragments/chatRoom :: publicRoom";
    }

    @MessageMapping("register")
    @SendTo("/topic/public")
    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        chatMessage.setTime(new Date());
        return chatMessage;
    }

    @MessageMapping("send")
    @SendTo("/topic/public")
    public ChatMessage sendPublicMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setTime(new Date());
        return chatMessage;
    }

    @MessageMapping("/secured/room-{username}")
    @SendTo("/secured/user/queue/specific-user-user{username}")
    public ChatMessage sendSpecific(
            @DestinationVariable String username,
            @Payload ChatMessage chatMessage,
            Principal user,
            @Header("simpSessionId") String sessionId) throws Exception {
        chatMessage.setTime(new Date());
        System.out.println("p2pChat");
        System.out.println(chatMessage.getSender());
        System.out.println(chatMessage.getReceiver());
        System.out.println("/secured/user/queue/specific-user" + "-user" + chatMessage.getReceiver());
        simpMessagingTemplate.convertAndSendToUser(
                chatMessage.getReceiver(), "/secured/user/queue/specific-user" + "-user" + chatMessage.getReceiver(), chatMessage);
        return chatMessage;
    }
}
