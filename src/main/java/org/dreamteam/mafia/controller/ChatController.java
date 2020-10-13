package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.model.Game;
import org.dreamteam.mafia.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.ArrayList;

@Controller
public class ChatController {

    ArrayList<String> names = new ArrayList<>();

    @MessageMapping("/civ_message")
    @SendTo("/chat/civ_messages")
    public Message getCiviliansMessages(Message message) {
        message.setRole(Message.Role.CIVILIAN);
        System.out.println(message.getFrom() + " " + message.getMessage());
        if(!names.contains(message.getFrom())) names.add(message.getFrom());
        System.out.println(names);
        return message;
    }


    @MessageMapping("/mafia_message")
    @SendTo("/chat/mafia_messages")
    public Message getMafiaMessages(Message message) {
        message.setRole(Message.Role.MAFIA);
        if(!names.contains(message.getFrom())) names.add(message.getFrom());
        System.out.println(names);
        return message;
    }

    Game game = new Game();
    @Scheduled(fixedDelay = 1000)
    //@MessageMapping("/game_stat")
    @SendTo("/chat/game_stat")
    public Game getGame() {
        System.out.println("lol");
        game.setNight(!game.isNight());
        return game;
    }
}
