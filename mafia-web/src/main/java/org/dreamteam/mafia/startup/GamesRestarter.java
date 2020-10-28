package org.dreamteam.mafia.startup;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.dreamteam.mafia.service.api.MessageService;
import org.dreamteam.mafia.service.implementation.GameEngine.GameHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GamesRestarter implements
        ApplicationListener<ContextRefreshedEvent> {

    private final RoomRepository roomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @Autowired
    public GamesRestarter(
            RoomRepository roomRepository, SimpMessagingTemplate messagingTemplate,
            MessageService messageService) {
        this.roomRepository = roomRepository;
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final List<RoomDAO> rooms = roomRepository.findRoomDAOByGameStatus(GameStatusEnum.IN_PROGRESS);
        for (RoomDAO room : rooms) {
            GameHost gameHost = new GameHost(messagingTemplate, room, roomRepository, messageService);
            Thread thread = new Thread(gameHost);
            thread.start();
        }
    }
}
