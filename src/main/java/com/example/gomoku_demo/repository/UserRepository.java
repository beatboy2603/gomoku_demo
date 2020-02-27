package com.example.gomoku_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gomoku_demo.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findUserById(Long id);

    @Query(value = "select * from user where id in (select distinct(sender_id) from chatmessage "
            + "where receiver_id = :receiverId and (ISNULL(hasRead) or hasRead = false))", nativeQuery = true)
    List<User> getUsersWithNewMessages(@Param("receiverId") Long receiverId);

    @Query(value = "with newMess as (select distinct(sender_id) from chatmessage "
            + "where receiver_id = :receiverId and (ISNULL(hasRead) or hasRead = false)) "
            
            + "select id, (case "
            + "when id in (select * from newMess) then true "
            + "else false "
            + "end) as hasNewMess from user ORDER BY id", nativeQuery = true)
    List<User> getUsersWithNewMessagesTemp(@Param("receiverId") Long receiverId);

}
