/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.gomoku_demo.model;

import java.util.Date;
import lombok.Data;

/**
 *
 * @author ADMIN
 */
@Data
public class ChatMessage {
    private String content;
    private String sender;
    private String receiver;
    private Date time;
    private MessageType messageType;
    
    
    public enum MessageType{
        CHAT, JOIN, LEAVE
    }
}
