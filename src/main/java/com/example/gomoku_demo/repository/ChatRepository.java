/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.gomoku_demo.repository;

import com.example.gomoku_demo.model.ChatMessage;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ADMIN
 */
@Repository
public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

    @Query(value = "select * from chatmessage where messageType = 0 and "
            + "(sender_id = :senderId and receiver_id= :receiverId) or(sender_id = :receiverId and receiver_id=:senderId) "
            + "ORDER BY time", nativeQuery = true)
    List<ChatMessage> getPrivateMessages(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    @Transactional
    @Modifying
    @Query(value = "update chatMessage set hasRead = true "
            + "where receiver_id = :senderId and sender_id=:receiverId and (ISNULL(hasRead) or hasRead = false)", nativeQuery = true)
    void updateHasRead(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
}
