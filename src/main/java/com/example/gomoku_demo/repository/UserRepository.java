package com.example.gomoku_demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.gomoku_demo.model.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
	
	@Query(value = "select* from user where id != :userId and id not in \n" + 
			"(SELECT f.userId2 as id FROM friend f WHERE f.userId1= :userId  and isFriend = true\n" + 
			"union\n" + 
			"SELECT f.userId1 as id FROM friend f WHERE f.userId2= :userId  and isFriend = true)", nativeQuery = true)
	List<User> findFriends(@Param("userId") Long userId);
	
}
