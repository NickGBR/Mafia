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

    @Autowired
    SimpMessagingTemplate messagingTemplate;
    ArrayList<String> names = new ArrayList<>();

    @MessageMapping("/civ_message")
    //@SendTo("/chat/civ_messages") //Можем использовать как комнату по умолчанию
    public void getCiviliansMessages(Message message) {
        message.setRole(Message.Role.CIVILIAN);
        messagingTemplate.convertAndSend("/chat/civ_messages/" + message.getRoom(), message);
        if(!names.contains(message.getFrom())) names.add(message.getFrom());
    }


    @MessageMapping("/mafia_message")
    //@SendTo("/chat/mafia_messages/")  //Можем использовать как комнату по умолчанию
    public void getMafiaMessages(Message message) {
        message.setRole(Message.Role.MAFIA);
        messagingTemplate.convertAndSend("/chat/mafia_messages/" + message.getRoom(), message);
        if(!names.contains(message.getFrom())) names.add(message.getFrom());
    }
}
