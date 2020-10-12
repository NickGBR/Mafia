package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @MessageMapping("/message")
    @SendTo("/chat/civ_messages")
    public Message getCiviliansMessages(Message message) {
        System.out.println(message);
        return message;
    }

    @MessageMapping("/message")
    @SendTo("/chat/mafia_messages")
    public Message getMafiaMessages(Message message) {
        System.out.println(message);
        return message;
    }
}
