package com.example.gomoku_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.gomoku_demo.model.CustomUserDetail;
import com.example.gomoku_demo.model.User;
import com.example.gomoku_demo.service.UserService;

@Controller
public class FriendController {
	
	@Autowired
	UserService userService;
	
	
	@GetMapping("/addFriend")
	public String addFriend(Model model, Authentication auth) {
		List<User> users = userService.
				findFriends(
						((CustomUserDetail)auth.getPrincipal()).getId()
								);
		
		model.addAttribute("users", users);
		return "friend_request";
	}
}
