package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.model.Game;
import org.dreamteam.mafia.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

@Controller
public class ChatController {

    ArrayList<String> names = new ArrayList<>();

    @MessageMapping("/civ_message")
    @SendTo("/chat/civ_messages")
    public Message getCiviliansMessages(Message message) {
        message.setRole(Message.Role.CIVILIAN);
        if(!names.contains(message.getFrom())) names.add(message.getFrom());
        return message;
    }


    @MessageMapping("/mafia_message")
    @SendTo("/chat/mafia_messages")
    public Message getMafiaMessages(Message message) {
        message.setRole(Message.Role.MAFIA);
        if(!names.contains(message.getFrom())) names.add(message.getFrom());
        return message;
    }
}
