package org.dreamteam.mafia.model;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

public class Host implements Runnable{
    private final String room;
    private final SimpMessagingTemplate messagingTemplate;
    Game game;

    public Host(String room, SimpMessagingTemplate messagingTemplate) {
        this.room = room;
        this.messagingTemplate = messagingTemplate;
        this.game = new Game();
        game.setNight(true);
        this.game.setRoom(room);
    }

    @Override
    public void run() {
        game.setNight(!game.isNight());
        messagingTemplate.convertAndSend("/chat/game_stat/" + room, game);
    }
}