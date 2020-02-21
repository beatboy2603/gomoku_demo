/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.gomoku_demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
public class ChatRestController {
    
//    @PostMapping("/chat/registerName")
//    public void registerName(HttpSession session, @RequestBody String map, Model model){
//        System.out.println("---------------");
//        System.out.println(map);
//        System.out.println("+++++++++++++++");
////        session.setAttribute("username", map);
//        model.addAttribute("username", map);
////        return "chat";
//    } 
}
