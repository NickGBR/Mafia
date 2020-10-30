package org.dreamteam.mafia.service.implementation.engine;

import lombok.SneakyThrows;
import org.dreamteam.mafia.dto.CharacterUpdateDTO;
import org.dreamteam.mafia.dto.GameDTO;
import org.dreamteam.mafia.entities.RoomEntity;
import org.dreamteam.mafia.entities.UserEntity;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.model.*;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.dreamteam.mafia.service.api.GameService;
import org.dreamteam.mafia.service.api.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GameHost implements Runnable {
    private final GameDTO gameDTO = new GameDTO();
    private RoomEntity room;
    private final RoomRepository roomRepository;
    private int dayCounter;
    private final MessageService messageService;
    private final GameService gameService;
    private final Map<String, Integer> durations;
    private final Logger logger = LoggerFactory.getLogger(GameHost.class);

    public GameHost(
            RoomEntity room, RoomRepository roomRepository, MessageService messageService,
            GameService gameService, GamePhaseDurationsService durationsService) {
        this.room = room;
        this.roomRepository = roomRepository;
        dayCounter = room.getDayNumber();
        this.messageService = messageService;
        this.gameService = gameService;
        this.durations = durationsService.getDurations();
    }

    @SneakyThrows
    @Override
    public void run() {
        gameDTO.setGamePhase(room.getGamePhase());
        Thread.currentThread().setName(room.getRoomId().toString());

        Thread.sleep(durations.get("GRACE_DURATION") * 1000);
        while (room.getGameStatus().equals(GameStatusEnum.IN_PROGRESS)) {
            switch (room.getGamePhase()) {
                case CIVILIANS_DISCUSS_PHASE:
                    dayCounter++;
                    civiliansDiscussPhase(durations.get("CIVILIAN_DISCUSS_PHASE_DURATION"));
                    if (dayCounter == 1) {
                        goToPhase(GamePhaseEnum.MAFIA_DISCUSS_PHASE);
                    } else {
                        goToPhase(GamePhaseEnum.CIVILIANS_VOTE_PHASE);
                    }
                    break;

                case CIVILIANS_VOTE_PHASE:
                    civiliansVotePhase(durations.get("CIVILIAN_VOTING_PHASE_DURATION"));
                    room = roomRepository.findById(room.getRoomId()).get();
                    getVotingResult();
                    room = roomRepository.save(room);
                    goToPhase(GamePhaseEnum.MAFIA_DISCUSS_PHASE);
                    break;

                case MAFIA_DISCUSS_PHASE:
                    mafiaDiscussPhase(durations.get("MAFIA_DISCUSS_PHASE_DURATION"));
                    goToPhase(GamePhaseEnum.MAFIA_VOTE_PHASE);
                    break;

                case MAFIA_VOTE_PHASE:
                    mafiaVotePhase(durations.get("MAFIA_VOTING_PHASE_DURATION"));
                    room = roomRepository.findById(room.getRoomId()).get();
                    getVotingResult();
                    room = roomRepository.save(room);
                    goToPhase(GamePhaseEnum.DON_PHASE);
                    break;

                case DON_PHASE:
                    donPhase(durations.get("DON_PHASE_DURATION"));
                    goToPhase(GamePhaseEnum.SHERIFF_PHASE);
                    break;

                case SHERIFF_PHASE:
                    sheriffPhase(durations.get("SHERIFF_PHASE_DURATION"));
                    goToPhase(GamePhaseEnum.CIVILIANS_DISCUSS_PHASE);
                    break;
                case END_GAME_PHASE: {
                    room = completeRoom();
                    break;
                }
                default:
                    System.out.println("КОНЕЧНЫЙ АВТОМАТ СЛОМАЛСЯ");
            }
            Thread.sleep(durations.get("GRACE_DURATION") * 1000);
            logger.trace("Room at the end of phase: " + room);
            room = roomRepository.save(room);
        }
        messageService.sendSystemMessage(gameDTO,
                                         new MessageDestinationDescriptor(DestinationEnum.CIVILIAN, room));
        System.out.println("Thread " + Thread.currentThread().getName() + " has been stopped!");
    }

    /**
     * Переходит в уезынный этам игры, если не одна из сторон
     * не победила после голосования.
     *
     * @param phase
     */
    private void goToPhase(GamePhaseEnum phase) {
        switch (gameService.isMafiaVictoryInRoom(room)) {
            case CIVILIANS_WON:
                room.setGamePhase(phase);
                gameDTO.setGamePhase(GamePhaseEnum.END_GAME_PHASE);
                gameDTO.setMessage("Мирные победили, УРА!");
                room.setGamePhase(GamePhaseEnum.END_GAME_PHASE);
                break;
            case MAFIA_WON:
                gameDTO.setMessage("Мафия наказала мирных!");
                gameDTO.setGamePhase(GamePhaseEnum.END_GAME_PHASE);
                room.setGamePhase(GamePhaseEnum.END_GAME_PHASE);

                break;
            case GAME_NOT_ENDED:
                gameDTO.setGamePhase(phase);
                room.setGamePhase(phase);
                break;
            default:
                break;
        }
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

    private void endGame(GameEndStatus reason) {
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

    private RoomEntity completeRoom() {
        room.setGameStatus(GameStatusEnum.COMPLETED);
        for (UserEntity user : room.getUserList()) {
            user.setCharacterStatus(CharacterStatusEnum.ALIVE);
            user.setCharacter(CharacterEnum.CITIZEN);
            user.setIsReady(false);
            user.setRoom(null);
            user.setIsAdmin(false);
        }
        return room;
    }

    private void getVotingResult() throws ClientErrorException, InterruptedException {

        ArrayList<VotingResult> result = new ArrayList<>();

        // Записываем результаты голосования.
        for (UserEntity userEntity : room.getUserList()) {
            VotingResult votingResult = new VotingResult();
            votingResult.setLogin(userEntity.getLogin());
            votingResult.setResult(userEntity.getVotesAgainst());
            result.add(votingResult);
        }

        // Сортируем результат голосования.
        result.sort(Collections.reverseOrder(Comparator.comparing(VotingResult::getResult)));

        if (!checkTie(result)) {
            final UserEntity victim = KillUser(result);
            messageService.sendVotingResultUpdate(new CharacterUpdateDTO(victim), room);
            Thread.sleep(durations.get("GRACE_DURATION") * 1000);
        } else {
            messageService.sendVotingResultUpdate(new CharacterUpdateDTO(), room);
        }
        refreshVotingResult(result);
    }

    /**
     * Проверияем на ничью
     */

    private boolean checkTie(ArrayList<VotingResult> result) throws ClientErrorException {
        if (result.get(0).getResult().intValue() == result.get(1).getResult().intValue()) {
            return true;
        }
        return false;
    }

    private UserEntity KillUser(ArrayList<VotingResult> result) throws ClientErrorException {
        String login = result.get(0).getLogin();
        final Optional<UserEntity> victim = room.getUserList().stream()
                .filter(
                        (dao) -> dao.getLogin().equals(login))
                .findFirst();
        if (!victim.isPresent()) {
            throw new RuntimeException(
                    "Server internal logic error. User, that was voted out? is not present in the room");
        }
        victim.get().setCharacterStatus(CharacterStatusEnum.DEAD);
        ;
        return victim.get();
    }

    /**
     * Обновляет данные просле этапа голосования.
     *
     * @param result список пользователей комнаты, для обновления.
     */
    private void refreshVotingResult(ArrayList<VotingResult> result) {
        for (UserEntity userEntity : room.getUserList()) {
            userEntity.setVotesAgainst(0);
            userEntity.setHasVoted(false);
        }
    }
}



