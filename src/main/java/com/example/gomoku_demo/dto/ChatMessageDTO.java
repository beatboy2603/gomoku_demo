/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.gomoku_demo.dto;

import com.example.gomoku_demo.model.User;
import java.util.Date;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

/**
 *
 * @author ADMIN
 */
@Data
public class ChatMessageDTO {
    
    private Long id;

    private String content;
    private UserDTO sender;
    private UserDTO receiver;
    private Date time;
    private MessageType messageType;
    private Boolean hasRead;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }
}
