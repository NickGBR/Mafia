package org.dreamteam.mafia.model;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

public class Host implements Runnable{

    private final String room;
    private final SimpMessagingTemplate messagingTemplate; // Используется для отправки сообщений клиенту

    private final Game game;
    private final Map<String, Message> hostMessages;

    int i = 0; //// remove

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

        // Проверяем наличие сообщение от HOST в "hostMessages - бд", отправляем его при наличии.
        if(hostMessages.containsKey(room)){
            messagingTemplate.convertAndSend("/chat/civ_messages/" + room, hostMessages.get(game.getRoom()));
            //Удаляем сообщение из бд.
            hostMessages.remove(room);
        }
        game.setNight(!game.isNight());
        //System.out.println(room + " ooops " + i);//remove!!!
        //i++; ///remove!
        messagingTemplate.convertAndSend("/chat/game_stat/" + room, game);
    }
}