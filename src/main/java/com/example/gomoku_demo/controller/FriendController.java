package com.example.gomoku_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FriendController {

    @GetMapping("/addFriend")
    public String addFriend() {
        return "friend_request";
    }
}
