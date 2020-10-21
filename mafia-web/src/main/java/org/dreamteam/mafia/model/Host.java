package org.dreamteam.mafia.model;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

public class Host implements Runnable{

    private final SimpMessagingTemplate messagingTemplate; // Используется для отправки сообщений клиенту

    private final Room room;
    private final Map<String, Message> hostMessages;

    int i = 0; //// remove

    public Host(String room, SimpMessagingTemplate messagingTemplate, Map<String, Message> hostMessages) {
        this.hostMessages = hostMessages;
        this.messagingTemplate = messagingTemplate;
        this.room = new Room();
        this.room.setNight(true);
        this.room.setName(room);

    }

    @Override
    public void run() {

        // Проверяем наличие сообщение от HOST в "hostMessages - бд", отправляем его при наличии.
        if(hostMessages.containsKey(room)){
            messagingTemplate.convertAndSend("/chat/civ_messages/" + room, hostMessages.get(room.getName()));
            //Удаляем сообщение из бд.
            hostMessages.remove(room);
        }
        room.setNight(!room.isNight());
        //System.out.println(room + " ooops " + i);//remove!!!
        //i++; ///remove!
        messagingTemplate.convertAndSend("/chat/game_stat/" + room, room);
    }
}