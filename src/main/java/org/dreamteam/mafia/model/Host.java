package org.dreamteam.mafia.model;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.Map;

public class Host implements Runnable{
    private final String room;
    private final SimpMessagingTemplate messagingTemplate;
    Game game;
    Map<String, Message> hostMessages;
    public Host(String room, SimpMessagingTemplate messagingTemplate, Map<String, Message> hostMessages) {
        this.hostMessages = hostMessages;
        this.room = room;
        this.messagingTemplate = messagingTemplate;
        this.game = new Game();
        game.setNight(true);
        this.game.setRoom(room);
    }

    @Override
    public void run() {
        System.out.println(hostMessages.size());

        // Проверяем наличие сообщение от HOST в базе, отправляем его при наличии.
        if(hostMessages.containsKey(room)){
            messagingTemplate.convertAndSend("/chat/civ_messages/" + room, hostMessages.get(game.getRoom()));
            hostMessages.remove(room);
        }
        game.setNight(!game.isNight());
        messagingTemplate.convertAndSend("/chat/game_stat/" + room, game);
    }
}