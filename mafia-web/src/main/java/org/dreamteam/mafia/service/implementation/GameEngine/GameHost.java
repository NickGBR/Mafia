package org.dreamteam.mafia.service.implementation.GameEngine;

import lombok.SneakyThrows;
import org.dreamteam.mafia.constants.SockConst;
import org.dreamteam.mafia.dao.enums.GamePhaseEnum;
import org.dreamteam.mafia.dto.GameDTO;
import org.dreamteam.mafia.service.api.UserService;
import org.springframework.messaging.simp.SimpMessagingTemplate;


public class GameHost implements Runnable {
    private final SimpMessagingTemplate messagingTemplate; // Используется для отправки сообщений клиенту
    private final GameDTO gameDTO = new GameDTO();
    private final Long roomID;

    public GameHost(SimpMessagingTemplate messagingTemplate,
                    UserService userService,
                    Long roomID) {
        this.messagingTemplate = messagingTemplate;
        this.roomID = roomID;
    }

    @SneakyThrows
    @Override
    public void run() {
        Thread.currentThread().setName(roomID.toString());
        while (true) {
            gameDTO.setGamePhase(GamePhaseEnum.CIVILIANS_PHASE);
            messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + roomID, gameDTO);
            gameDTO.setGamePhase(GamePhaseEnum.MAFIA_PHASE);

            Thread.sleep(10000);
            messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + roomID, gameDTO);
            gameDTO.setGamePhase(GamePhaseEnum.DON_PHASE);

            Thread.sleep(10000);
            messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + roomID, gameDTO);
            gameDTO.setGamePhase(GamePhaseEnum.SHERIFF_PHASE);

            Thread.sleep(10000);
            messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + roomID, gameDTO);

            Thread.sleep(10000);
        }
    }
}