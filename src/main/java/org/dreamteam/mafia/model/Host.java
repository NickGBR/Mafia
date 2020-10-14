package org.dreamteam.mafia.model;

import org.dreamteam.mafia.model.Game;
import org.dreamteam.mafia.model.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;

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
        this.game.setActive(true);
    }

    @Override
    public void run() {
        System.out.println(room + " asked!");
        game.setNight(!game.isNight());
        messagingTemplate.convertAndSend("/chat/game_stat/" + room, game);
    }
}