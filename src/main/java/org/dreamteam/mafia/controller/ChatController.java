package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/chat/civ_messages")
    public Message getCiviliansMessages(Message message) {
        message.setRole(Message.Role.CIVILIAN);
        System.out.println(message);
        return message;
    }
    

    @MessageMapping("/chat-messaging")
    public void sendSpecific(
            @Payload Message msg,
            Principal user,
            @Header("simpSessionId") String sessionId) throws Exception {
        simpMessagingTemplate.convertAndSendToUser(
                msg.getFrom(), "/secured/user/queue/specific-user", msg);
    }
}
