/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.gomoku_demo.controller;

import com.example.gomoku_demo.model.ChatMessage;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADMIN
 */


@Controller
public class ChatController {
    
    @GetMapping("/chat")
    public String goToChat(){
        return "chat";
    }
    
    @PostMapping("/chat/registerName")
    public String registerName(@RequestBody HashMap<String, String> json, Model model){
//    public String registerName(@RequestParam("username") String json, Model model){
        System.out.println("---------------");
        System.out.println("+++++++++++++++");
        model.addAttribute("username", json.get("username"));
        return "fragments/chatRoom :: publicRoom";
    }
    
    @MessageMapping("register")
    @SendTo("/room/public")
    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
    
    @MessageMapping("send")
    @SendTo("/room/public")
    public ChatMessage sendPublicMessage(@Payload ChatMessage chatMessage){
        return chatMessage;
    }
}
