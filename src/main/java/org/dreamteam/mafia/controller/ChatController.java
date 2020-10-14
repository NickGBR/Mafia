package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.model.Game;
import org.dreamteam.mafia.model.Host;
import org.dreamteam.mafia.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

@Controller
public class ChatController {

    @Qualifier("Task")
    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    SimpMessagingTemplate messagingTemplate;
    ArrayList<String> rooms = new ArrayList<>();

    @MessageMapping("/civ_message")
    //@SendTo("/chat/civ_messages") //Можем использовать как комнату по умолчанию
    public void getCiviliansMessages(Message message) {
        message.setRole(Message.Role.CIVILIAN);
        messagingTemplate.convertAndSend("/chat/civ_messages/" + message.getRoom(), message);
    }


    @MessageMapping("/mafia_message")
    //@SendTo("/chat/mafia_messages/")  //Можем использовать как комнату по умолчанию
    public void getMafiaMessages(Message message) {
        System.out.println(message.getRoom());
        message.setRole(Message.Role.MAFIA);
        messagingTemplate.convertAndSend("/chat/mafia_messages/" + message.getRoom(), message);
    }

    @MessageMapping("/host_message")
    //@SendTo("/chat/mafia_messages/")  //Можем использовать как комнату по умолчанию
    public void getHostMessages(Game game) {
        if (!rooms.contains(game.getRoom())) {
            System.out.println(game.getRoom() + " added");
            rooms.add(game.getRoom());
            taskScheduler.scheduleWithFixedDelay(new Host(game.getRoom(), messagingTemplate),10000);
        }
        else {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            messagingTemplate.convertAndSend("/chat/mafia_messages/" + game.getRoom(), "{\"active\":true}");
        }
    }
}
