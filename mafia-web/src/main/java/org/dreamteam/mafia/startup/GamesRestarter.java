package org.dreamteam.mafia.startup;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.dreamteam.mafia.service.api.GameService;
import org.dreamteam.mafia.service.api.MessageService;
import org.dreamteam.mafia.service.implementation.engine.GameHost;
import org.dreamteam.mafia.service.implementation.engine.GamePhaseDurationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GamesRestarter implements
        ApplicationListener<ContextRefreshedEvent> {

    private final RoomRepository roomRepository;
    private final MessageService messageService;
    private final GameService gameService;
    private final GamePhaseDurationsService durationsService;

    @Autowired
    public GamesRestarter(
            RoomRepository roomRepository,
            MessageService messageService, GameService gameService,
            GamePhaseDurationsService durationsService) {
        this.roomRepository = roomRepository;
        this.messageService = messageService;
        this.gameService = gameService;
        this.durationsService = durationsService;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final List<RoomDAO> rooms = roomRepository.findRoomDAOByGameStatus(GameStatusEnum.IN_PROGRESS);
        for (RoomDAO room : rooms) {
            GameHost gameHost = new GameHost(room, roomRepository, messageService, gameService,
                                             durationsService
            );
            Thread thread = new Thread(gameHost);
            thread.start();
        }
    }
}
