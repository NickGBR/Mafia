package org.dreamteam.mafia.model;

import lombok.SneakyThrows;
import org.dreamteam.mafia.dao.enums.GamePhaseEnum;
import org.dreamteam.mafia.dto.GameDTO;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.service.api.UserService;
import org.springframework.messaging.simp.SimpMessagingTemplate;


public class GameHost implements Runnable {
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate; // Используется для отправки сообщений клиенту
    private final GameDTO gameDTO = new GameDTO();

    public GameHost(SimpMessagingTemplate messagingTemplate,
                    UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;


    }

    @SneakyThrows
    @Override
    public void run() {
        Long roomId = userService.getCurrentUserDAO().get().getRoom().getRoomId();
        Thread.currentThread().setName(roomId.toString());
        while (true) {
            gameDTO.setGamePhase(GamePhaseEnum.CIVILIANS_PHASE);
            messagingTemplate.convertAndSend("/chat/civ_messages/" + roomId, GamePhaseEnum.CIVILIANS_PHASE);
            gameDTO.setGamePhase(GamePhaseEnum.MAFIA_PHASE);

            Thread.sleep(10000);
            messagingTemplate.convertAndSend("/chat/civ_messages/" + roomId, GamePhaseEnum.MAFIA_PHASE);
            gameDTO.setGamePhase(GamePhaseEnum.DON_PHASE);

            Thread.sleep(10000);
            messagingTemplate.convertAndSend("/chat/civ_messages/" + roomId, GamePhaseEnum.DON_PHASE);
            gameDTO.setGamePhase(GamePhaseEnum.SHERIFF_PHASE);

            Thread.sleep(10000);
            messagingTemplate.convertAndSend("/chat/civ_messages/" + roomId, GamePhaseEnum.SHERIFF_PHASE);

            Thread.sleep(10000);
        }
    }
}