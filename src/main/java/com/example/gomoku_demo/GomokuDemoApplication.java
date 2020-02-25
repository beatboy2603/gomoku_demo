package com.example.gomoku_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.gomoku_demo.model.User;
import com.example.gomoku_demo.repository.UserRepository;



@SpringBootApplication
public class GomokuDemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GomokuDemoApplication.class, args);
	}
	
	@Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setUsername("nghi");
        user.setPassword(passwordEncoder.encode("1"));
        User user1 = new User();
        user1.setUsername("no");
        user1.setPassword(passwordEncoder.encode("1"));
        User user2 = new User();
        user2.setUsername("chan");
        user2.setPassword(passwordEncoder.encode("1"));
        userRepository.save(user);
        userRepository.save(user1);
        userRepository.save(user2);
        System.out.println(user);
    }

}
