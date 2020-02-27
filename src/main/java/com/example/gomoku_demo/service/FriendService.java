package com.example.gomoku_demo.service;

import com.example.gomoku_demo.model.User;
import com.example.gomoku_demo.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {
	
	@Autowired
        private UserRepository userRepository;
        
        public List<User> getUsersWithNewMessages(Long receiverId){
            return userRepository.getUsersWithNewMessages(receiverId);
        }
	
}
