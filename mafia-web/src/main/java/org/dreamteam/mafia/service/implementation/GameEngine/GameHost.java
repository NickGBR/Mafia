package org.dreamteam.mafia.service.implementation.GameEngine;

import lombok.SneakyThrows;
import org.dreamteam.mafia.constants.GameConst;
import org.dreamteam.mafia.constants.SockConst;
import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dao.enums.EndGameReasons;
import org.dreamteam.mafia.dao.enums.GamePhaseEnum;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;
import org.dreamteam.mafia.dto.GameDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;


public class GameHost implements Runnable {
    private final SimpMessagingTemplate messagingTemplate; // Используется для отправки сообщений клиенту
    private final GameDTO gameDTO = new GameDTO();
    private final UserDAO user;
    private int dayCounter = 0;

    public GameHost(SimpMessagingTemplate messagingTemplate,
                    UserDAO user) {
        this.messagingTemplate = messagingTemplate;
        this.user = user;
    }

    @SneakyThrows
    @Override
    public void run() {

        user.getRoom().setGameStatus(GameStatusEnum.IN_PROGRESS);
        RoomDAO roomDAO = user.getRoom();
        gameDTO.setGamePhase(roomDAO.getGamePhase());
        Thread.currentThread().setName(user.getRoom().getRoomId().toString());
        Thread.sleep(5000);


        while (user.getRoom().getGameStatus().equals(GameStatusEnum.IN_PROGRESS)) {
            switch (roomDAO.getGamePhase()) {
                case CIVILIANS_PHASE:
                    dayCounter++;
                    civiliansPhase(user, gameDTO, 1);
                    break;
                case MAFIA_PHASE:
                    mafiaPhase(user, gameDTO, 1);
                    break;
                case DON_PHASE:
                    donPhase(user, gameDTO, 1);
                    break;
                case SHERIFF_PHASE:
                    sheriffPhase(user, gameDTO, 1);
                    if (isFinalDay(dayCounter)) endGame(user, gameDTO, EndGameReasons.LAST_DAY_CAME);
                    break;
                default:
                    System.out.println("КОНЕЧНЫЙ АВТОМАТ СЛОМАЛСЯ");
            }
        }
        System.out.println("Thread " + Thread.currentThread().getName() + " has been stopped!");
    }

    @SneakyThrows
    private void mafiaPhase(UserDAO user, GameDTO gameDTO, int phaseTimeSec) {


        gameDTO.setMessage("Ночь грядет, мафия в бой идет! ");
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + user.getRoom().getRoomId(), gameDTO);

        Thread.sleep(phaseTimeSec * 1000);

        gameDTO.setGamePhase(GamePhaseEnum.DON_PHASE);
        user.getRoom().setGamePhase(gameDTO.getGamePhase());
    }

    @SneakyThrows
    private void civiliansPhase(UserDAO user, GameDTO gameDTO, int phaseTimeSec) {

        user.getRoom().setDayNumber(dayCounter);

        gameDTO.setMessage("Доброе утро ребята это " + dayCounter + " день!");
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + user.getRoom().getRoomId(), gameDTO);

        Thread.sleep(phaseTimeSec * 1000);

        gameDTO.setGamePhase(GamePhaseEnum.MAFIA_PHASE);
        user.getRoom().setGamePhase(gameDTO.getGamePhase());
    }

    @SneakyThrows
    private void donPhase(UserDAO user, GameDTO gameDTO, int phaseTimeSec) {
        gameDTO.setMessage("Время дона разобраться с шерифом!");
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + user.getRoom().getRoomId(), gameDTO);

        Thread.sleep(phaseTimeSec * 1000);

        gameDTO.setGamePhase(GamePhaseEnum.SHERIFF_PHASE);
        user.getRoom().setGamePhase(gameDTO.getGamePhase());
    }

    @SneakyThrows
    private void sheriffPhase(UserDAO user, GameDTO gameDTO, int phaseTimeSec) {
        gameDTO.setMessage("Шериф, за работу!!!");
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + user.getRoom().getRoomId(), gameDTO);

        Thread.sleep(phaseTimeSec * 1000);

        gameDTO.setGamePhase(GamePhaseEnum.CIVILIANS_PHASE);
        user.getRoom().setGamePhase(gameDTO.getGamePhase());
    }

    private void endGame(UserDAO user, GameDTO gameDTO, EndGameReasons reason) {
        switch (reason) {
            case LAST_DAY_CAME:
                gameDTO.setMessage("Игра окончена, дни вышли.");
                user.getRoom().setGameStatus(GameStatusEnum.COMPLETED);
                messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + user.getRoom().getRoomId(), gameDTO);
                break;
        }
    }

    private boolean isFinalDay(int day) {
        if (day == GameConst.DURATION) {
            return true;
        }
        return false;
    }
}