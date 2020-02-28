package com.example.gomoku_demo.converter;

import org.springframework.stereotype.Component;

import com.example.gomoku_demo.dto.UserDTO;
import com.example.gomoku_demo.model.User;

@Component
public class UserConverter {

//	public User toEntity(UserDTO userDTO) {
//		User user = new User();
//		user.setUsername(userDTO.getUsername());
//		user.setPassword(password);
//		return user;
//	}
//	
	public UserDTO toDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(user.getUsername());
		return userDTO;
	}
}
