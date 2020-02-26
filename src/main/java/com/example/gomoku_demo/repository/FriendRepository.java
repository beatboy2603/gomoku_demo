package com.example.gomoku_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gomoku_demo.model.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

}
