package org.dreamteam.mafia.service.implementation.GameEngine;

import lombok.SneakyThrows;
import org.dreamteam.mafia.constants.GameConst;
import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dao.enums.*;
import org.dreamteam.mafia.dto.GameDTO;
import org.dreamteam.mafia.dto.VotingResultDTO;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.model.MessageDestinationDescriptor;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.dreamteam.mafia.service.api.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class GameHost implements Runnable {
    private final SimpMessagingTemplate messagingTemplate; // Используется для отправки сообщений клиенту
    private final GameDTO gameDTO = new GameDTO();
    private RoomDAO room;
    private final RoomRepository roomRepository;
    private int dayCounter;
    private final MessageService messageService;
    private final Logger logger = LoggerFactory.getLogger(GameHost.class);


    public GameHost(
            SimpMessagingTemplate messagingTemplate,
            RoomDAO room, RoomRepository roomRepository, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.room = room;
        this.roomRepository = roomRepository;
        dayCounter = room.getDayNumber();
        this.messageService = messageService;
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
                    goToPhase(GamePhaseEnum.CIVILIANS_VOTE_PHASE);
                    break;

                case CIVILIANS_VOTE_PHASE:
                    civiliansVotePhase(GameConst.CIVILIAN_VOTING_PHASE_DURATION);
                    room = roomRepository.findById(room.getRoomId()).get();
                    getVotingResult();
                    goToPhase(GamePhaseEnum.MAFIA_DISCUSS_PHASE);
                    break;

                case MAFIA_DISCUSS_PHASE:
                    mafiaDiscussPhase(GameConst.MAFIA_DISCUSS_PHASE_DURATION);
                    goToPhase(GamePhaseEnum.MAFIA_VOTE_PHASE);
                    break;

                case MAFIA_VOTE_PHASE:
                    mafiaVotePhase(GameConst.MAFIA_VOTING_PHASE_DURATION);
                    room = roomRepository.findById(room.getRoomId()).get();
                    getVotingResult();
                    goToPhase(GamePhaseEnum.DON_PHASE);
                    break;

                case DON_PHASE:
                    donPhase(GameConst.DON_PHASE_DURATION);
                    goToPhase(GamePhaseEnum.SHERIFF_PHASE);
                    break;

                case SHERIFF_PHASE:
                    sheriffPhase(GameConst.SHERIFF_PHASE_DURATION);
                    goToPhase(GamePhaseEnum.CIVILIANS_DISCUSS_PHASE);
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
        messageService.sendSystemMessage(gameDTO,
                                         new MessageDestinationDescriptor(DestinationEnum.CIVILIAN, room));
        System.out.println("Thread " + Thread.currentThread().getName() + " has been stopped!");
    }

    private void goToPhase(GamePhaseEnum phase) {
        gameDTO.setGamePhase(phase);
        room.setGamePhase(phase);
    }

    @SneakyThrows
    private void civiliansDiscussPhase(int phaseTimeSec) {

        room.setDayNumber(dayCounter);

        gameDTO.setMessage("Доброе утро ребята это " + dayCounter + " день!");

        // Передаем информация о длительность этапа.
        gameDTO.setTimer(phaseTimeSec * 1000);

        messageService.sendSystemMessage(gameDTO,
                                         new MessageDestinationDescriptor(DestinationEnum.CIVILIAN, room));

        Thread.sleep(phaseTimeSec * 1000);
    }

    @SneakyThrows
    private void civiliansVotePhase(int phaseTimeSec) {

        gameDTO.setMessage("Мирные, ваше время пришло, голосуйте!");

        // Передаем информация о длительность этапа.
        gameDTO.setTimer(phaseTimeSec * 1000);

        messageService.sendSystemMessage(gameDTO,
                                         new MessageDestinationDescriptor(DestinationEnum.CIVILIAN, room));
        Thread.sleep(phaseTimeSec * 1000);
    }

    @SneakyThrows
    private void mafiaDiscussPhase(int phaseTimeSec) {

        gameDTO.setMessage("Ночь грядет, мафия в бой идет! ");
        // Передаем информация о длительность этапа.
        gameDTO.setTimer(phaseTimeSec * 1000);

        messageService.sendSystemMessage(gameDTO,
                                         new MessageDestinationDescriptor(DestinationEnum.CIVILIAN, room));
        Thread.sleep(phaseTimeSec * 1000);

    }

    @SneakyThrows
    private void mafiaVotePhase(int phaseTimeSec) {


        gameDTO.setMessage("Мафия голосует! ");

        // Передаем информация о длительность этапа.
        gameDTO.setTimer(phaseTimeSec * 1000);

        messageService.sendSystemMessage(gameDTO,
                                         new MessageDestinationDescriptor(DestinationEnum.CIVILIAN, room));

        Thread.sleep(phaseTimeSec * 1000);
    }

    @SneakyThrows
    private void donPhase(int phaseTimeSec) {
        gameDTO.setMessage("Время дона разобраться с шерифом!");

        // Передаем информация о длительность этапа.
        gameDTO.setTimer(phaseTimeSec * 1000);

        messageService.sendSystemMessage(gameDTO,
                                         new MessageDestinationDescriptor(DestinationEnum.CIVILIAN, room));

        Thread.sleep(phaseTimeSec * 1000);
    }

    @SneakyThrows
    private void sheriffPhase(int phaseTimeSec) {
        gameDTO.setMessage("Шериф, за работу!!!");

        // Передаем информация о длительность этапа.
        gameDTO.setTimer(phaseTimeSec * 1000);

        messageService.sendSystemMessage(gameDTO,
                                         new MessageDestinationDescriptor(DestinationEnum.CIVILIAN, room));

        Thread.sleep(phaseTimeSec * 1000);
    }

    private void endGame(EndGameReasons reason) {
        switch (reason) {
            case LAST_DAY_CAME:
                gameDTO.setGamePhase(GamePhaseEnum.END_GAME_PHASE);
                gameDTO.setMessage("Игра окончена, дни вышли.");
                break;
            case MAFIA_WON:
                gameDTO.setGamePhase(GamePhaseEnum.END_GAME_PHASE);
                gameDTO.setMessage("Мафия побдила.");
                break;
            case CIVILIANS_WON:
                gameDTO.setGamePhase(GamePhaseEnum.END_GAME_PHASE);
                gameDTO.setMessage("Мирные победили.");
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

    private RoomDAO completeRoom() {
        room.setGameStatus(GameStatusEnum.COMPLETED);
        for (UserDAO user : room.getUserList()) {
            user.setIsReady(false);
            user.setRoom(null);
            user.setIsAdmin(false);
        }
        return room;
    }

    private void getVotingResult() throws ClientErrorException {

        ArrayList<VotingResultDTO> result = new ArrayList<>();

        // Записываем результаты голосования.
        for (UserDAO userDAO : room.getUserList()) {
            VotingResultDTO votingResult = new VotingResultDTO();
            votingResult.setLogin(userDAO.getLogin());
            votingResult.setResult(userDAO.getVotesAgainst());
            result.add(votingResult);
        }

        // Сортируем результат голосования.
        result.sort(Collections.reverseOrder(Comparator.comparing(VotingResultDTO::getResult)));

        if (!checkTie(result)) {
            KillUser(result);
        }
    }

    /**
     * Проверияем на ничью
     */

    private boolean checkTie(ArrayList<VotingResultDTO> result) throws ClientErrorException {
        if (result.get(0).getResult().intValue() == result.get(1).getResult().intValue()) {
            if (gameDTO.getGamePhase().equals(GamePhaseEnum.MAFIA_VOTE_PHASE)) {
                gameDTO.setMessage("Мафия не определилась с жертвой");
            } else {
                gameDTO.setMessage("Мирные не смогли определиться с выбором мафии");
            }
            messageService.sendSystemMessage(gameDTO,
                                             new MessageDestinationDescriptor(DestinationEnum.CIVILIAN, room));
            return true;
        }
        return false;
    }

    private void KillUser(ArrayList<VotingResultDTO> result) throws ClientErrorException {
        String login = result.get(0).getLogin();
        if (gameDTO.getGamePhase().equals(GamePhaseEnum.MAFIA_VOTE_PHASE)) {
            gameDTO.setMessage("Мафия убила " + login);
        } else {
            gameDTO.setMessage("Мирные убили " + login);
        }

        for (UserDAO userDAO : room.getUserList()) {
            userDAO.setVotesAgainst(0);
            if (userDAO.getLogin().equals(login)) {
                userDAO.setCharacterStatus(CharacterStatusEnum.DEAD);
            }
            System.out.println(userDAO.getVotesAgainst());
        }

        //roomRepository.findById(room.getRoomId()).get().setUserList(room.getUserList());
        messageService.sendSystemMessage(gameDTO,
                                         new MessageDestinationDescriptor(DestinationEnum.CIVILIAN, room));
    }

}



