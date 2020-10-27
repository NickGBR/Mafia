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
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;


public class GameHost implements Runnable {
    private final SimpMessagingTemplate messagingTemplate; // Используется для отправки сообщений клиенту
    private final GameDTO gameDTO = new GameDTO();
    private RoomDAO room;
    private final RoomRepository roomRepository;
    private int dayCounter;
    private final Logger logger = LoggerFactory.getLogger(GameHost.class);

    public GameHost(SimpMessagingTemplate messagingTemplate,
                    RoomDAO room, RoomRepository roomRepository) {
        this.messagingTemplate = messagingTemplate;
        this.room = room;
        this.roomRepository = roomRepository;
        dayCounter = room.getDayNumber();
    }

    @SneakyThrows
    @Override
    public void run() {

        gameDTO.setGamePhase(room.getGamePhase());
        Thread.currentThread().setName(room.getRoomId().toString());
        Thread.sleep(3000);

        while (room.getGameStatus().equals(GameStatusEnum.IN_PROGRESS)) {
            switch (room.getGamePhase()) {
                case CIVILIANS_DISCUSS_PHASE:
                    dayCounter++;
                    civiliansDiscussPhase(GameConst.CIVILIAN_DISCUSS_PHASE_DURATION);
                    break;
                case CIVILIANS_VOTE_PHASE:
                    civiliansVotePhase(GameConst.CIVILIAN_VOTING_PHASE_DURATION);
                    break;
                case MAFIA_DISCUSS_PHASE:
                    mafiaDiscussPhase(GameConst.MAFIA_DISCUSS_PHASE_DURATION);
                    break;
                case MAFIA_VOTE_PHASE:
                    mafiaVotePhase(GameConst.MAFIA_VOTING_PHASE_DURATION);
                    break;
                case DON_PHASE:
                    donPhase(GameConst.DON_PHASE_DURATION);
                    break;
                case SHERIFF_PHASE:
                    sheriffPhase(GameConst.SHERIFF_PHASE_DURATION);
                    if (isFinalDay(dayCounter)) {
                        endGame(EndGameReasons.LAST_DAY_CAME);
                    }
                    break;
                case END_GAME_PHASE: {
                    room = completeRoom();
                    break;
                }
                default:
                    System.out.println("КОНЕЧНЫЙ АВТОМАТ СЛОМАЛСЯ");
            }
            logger.debug("Room at the end of phase: " + room);
            room = roomRepository.save(room);
        }
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + room.getRoomId().toString(), gameDTO);
        System.out.println("Thread " + Thread.currentThread().getName() + " has been stopped!");
    }

    @SneakyThrows
    private void civiliansDiscussPhase(int phaseTimeSec) {

        room.setDayNumber(dayCounter);

        gameDTO.setMessage("Доброе утро ребята это " + dayCounter + " день!");
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + room.getRoomId(), gameDTO);

        Thread.sleep(phaseTimeSec * 1000);

        gameDTO.setGamePhase(GamePhaseEnum.CIVILIANS_VOTE_PHASE);
        room.setGamePhase(gameDTO.getGamePhase());
    }

    @SneakyThrows
    private void civiliansVotePhase(int phaseTimeSec) {

        gameDTO.setMessage("Мирные, ваше время пришло, голосуйте!");
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + room.getRoomId(), gameDTO);

        Thread.sleep(phaseTimeSec * 1000);

        gameDTO.setGamePhase(GamePhaseEnum.MAFIA_DISCUSS_PHASE);
        room.setGamePhase(gameDTO.getGamePhase());
    }

    @SneakyThrows
    private void mafiaDiscussPhase(int phaseTimeSec) {


        gameDTO.setMessage("Ночь грядет, мафия в бой идет! ");
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + room.getRoomId(), gameDTO);

        Thread.sleep(phaseTimeSec * 1000);

        gameDTO.setGamePhase(GamePhaseEnum.MAFIA_VOTE_PHASE);
        room.setGamePhase(gameDTO.getGamePhase());
    }

    @SneakyThrows
    private void mafiaVotePhase(int phaseTimeSec) {


        gameDTO.setMessage("Мафия голосует! ");
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + room.getRoomId(), gameDTO);

        Thread.sleep(phaseTimeSec * 1000);

        gameDTO.setGamePhase(GamePhaseEnum.DON_PHASE);
        room.setGamePhase(gameDTO.getGamePhase());
    }

    @SneakyThrows
    private void donPhase(int phaseTimeSec) {
        gameDTO.setMessage("Время дона разобраться с шерифом!");
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + room.getRoomId(), gameDTO);

        Thread.sleep(phaseTimeSec * 1000);

        gameDTO.setGamePhase(GamePhaseEnum.SHERIFF_PHASE);
        room.setGamePhase(gameDTO.getGamePhase());
    }

    @SneakyThrows
    private void sheriffPhase(int phaseTimeSec) {
        gameDTO.setMessage("Шериф, за работу!!!");
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + room.getRoomId(), gameDTO);

        Thread.sleep(phaseTimeSec * 1000);

        gameDTO.setGamePhase(GamePhaseEnum.CIVILIANS_DISCUSS_PHASE);
        room.setGamePhase(gameDTO.getGamePhase());
    }

    private void endGame(EndGameReasons reason) {
        switch (reason) {
            case LAST_DAY_CAME:
                gameDTO.setGamePhase(GamePhaseEnum.END_GAME_PHASE);
                gameDTO.setMessage("Игра окончена, дни вышли.");
                break;
        }
        room.setGamePhase(gameDTO.getGamePhase());
    }

    private boolean isFinalDay(int day) {
        if (day == GameConst.DURATION) {
            return true;
        }
        return false;
    }

    private RoomDAO completeRoom(){
        room.setGameStatus(GameStatusEnum.COMPLETED);
        for (UserDAO user : room.getUserList()) {
            user.setIsReady(false);
            user.setRoom(null);
            user.setIsAdmin(false);
        }
        return room;
    }
}