package com.example.gomoku_demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.gomoku_demo.model.CustomUserDetail;
import com.example.gomoku_demo.model.User;
import com.example.gomoku_demo.repository.UserRepository;



@Service
public class UserService implements UserDetailsService {

	@Autowired
    private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetail(user);
	}
	
	public User register(User user) {
		return userRepository.save(user);
	}
	
	public List<User> findFriends(Long userId) {
		List<User> users = new ArrayList<>();
		users = userRepository.findFriends(userId);
		return users;
	}

}
