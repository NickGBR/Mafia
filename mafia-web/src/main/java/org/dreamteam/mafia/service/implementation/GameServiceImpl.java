package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.constants.SockConst;
import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dao.enums.GamePhaseEnum;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;
import org.dreamteam.mafia.dto.CharacterDTO;
import org.dreamteam.mafia.exceptions.*;
import org.dreamteam.mafia.model.*;
import org.dreamteam.mafia.dao.enums.CharacterEnum;
import org.dreamteam.mafia.model.Character;
import org.dreamteam.mafia.repository.api.MessageRepository;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.service.api.GameService;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.util.ClientErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("gameService")
public class GameServiceImpl implements GameService {

    @Qualifier("Task")
    private final ThreadPoolTaskScheduler taskScheduler;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameServiceImpl(UserRepository userRepository,
                           RoomRepository roomRepository,
                           MessageRepository messageRepository,
                           UserService userService,
                           SimpMessagingTemplate messagingTemplate,
                           ThreadPoolTaskScheduler taskScheduler) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
        this.taskScheduler = taskScheduler;
    }


    /**
     * Меняет состояние игры на начатую,
     * проверят существует ли пользоватеь в базе данных,
     * проверяет достаточно ли у пльзователя прав для запуска комнаты,
     * отправляет всем пользователям информацию об успешном старте игры.
     */
    @Override
    public void startGame() throws ClientErrorException {

        Long roomId = userService.getCurrentUserDAO().get().getRoom().getRoomId();

        Optional<UserDAO> currentUserDAO = userService.getCurrentUserDAO();
        if (!currentUserDAO.isPresent()) {
            throw new SecurityException("User doesn't exist in a database");
        }
        if (currentUserDAO.get().getRoom() == null) {
            throw new ClientErrorException(ClientErrorCode.NOT_IN_ROOM, "User is not im the room");
        }
        RoomDAO room = currentUserDAO.get().getRoom();
        if (!currentUserDAO.get().getIsAdmin()) {
            throw new ClientErrorException((ClientErrorCode.NOT_ENOUGH_RIGHTS), "User isn't admin");
        }
        room.setGameStatus(GameStatusEnum.IN_PROGRESS);
        setRolesToUsers(room);
        roomRepository.save(room);
        messagingTemplate.convertAndSend(SockConst.SYS_GAME_STARTED_INFO + roomId, true);
        GameHost gameHost = new GameHost(messagingTemplate, userService);
    }

    /**
     * Возвращает список всех персонажей в игре
     *
     * @param room - игра
     * @return - список персонажей
     */
    @Override
    public List<Character> getCharactersInGame(Room room) {
        return null;
    }

    /**
     * Возвращает список всех сообщений в чате игры
     *
     * @param room - игра
     * @return - список сообщений
     */
    @Override
    public List<Message> getMessageLog(Room room) {
        return null;
    }

    /**
     * Переводит игру в следующую фазу
     *
     * @param room - игра
     * @throws ClientErrorException - если игра уже окончена
     */
    @Override
    public void advancePhase(Room room) throws ClientErrorException {

    }

    /**
     * Выдвигает персонажа на голосование
     *
     * @param user         - выдвигающий игрок
     * @param characterDTO - выдвигаемый персонаж
     * @throws IllegalMoveException - если выдвижение нарушает правила игры
     */
    @Override
    public void nominateCharacter(User user, CharacterDTO characterDTO) throws IllegalMoveException {

    }

    /**
     * Голосует против персонажа
     *
     * @param user         - голосующий игрок
     * @param characterDTO - голосуемый против персонаж
     * @throws IllegalMoveException - если голосование нарушает правила игры
     */
    @Override
    public void voteCharacter(User user, CharacterDTO characterDTO) throws IllegalMoveException {

    }

    public boolean isSheriff(String login) throws IllegalGamePhaseException, UserDoesNotExistInDBException,
            RoomsMismatchException, NotEnoughRightsException {
        Optional<UserDAO> userDAO = userRepository.findByLogin(login);
        if (!userDAO.isPresent()) {
            throw new UserDoesNotExistInDBException(ClientErrorCode.USER_NOT_EXISTS, "User \'" + login
                    + "\' doesn't exist in a database");
        }

        Optional<UserDAO> currentUserDAO = userService.getCurrentUserDAO();
        if (!currentUserDAO.isPresent()) {
            throw new UserDoesNotExistInDBException(ClientErrorCode.USER_NOT_EXISTS, "User doesn't exist in a database");
        }

        if (!Objects.equals(userDAO.get().getRoom().getRoomId(), currentUserDAO.get().getRoom().getRoomId())) {
            throw new RoomsMismatchException(ClientErrorCode.ROOMS_MISMATCH, "\'"
                    + userDAO.get().getLogin() + "\' and \'"
                    + currentUserDAO.get().getLogin() + "\' are in different rooms");
        }

        if (!currentUserDAO.get().getRoom().getGamePhase().equals(GamePhaseEnum.MAFIA_PHASE)) {
            throw new IllegalGamePhaseException(ClientErrorCode.WRONG_GAME_PHASE, "Wrong game phase");
        }

        if (!currentUserDAO.get().getCharacter().equals(org.dreamteam.mafia.dao.enums.CharacterEnum.DON)) {
            throw new NotEnoughRightsException("Permission denied");
        }

        return userDAO.get().getCharacter().equals(org.dreamteam.mafia.dao.enums.CharacterEnum.SHERIFF);

    }


    public boolean isMafia(String login) throws IllegalGamePhaseException, UserDoesNotExistInDBException,
            RoomsMismatchException, NotEnoughRightsException {
        Optional<UserDAO> userDAO = userRepository.findByLogin(login);
        if (!userDAO.isPresent()) {
            throw new UserDoesNotExistInDBException(ClientErrorCode.USER_NOT_EXISTS, "User \'" + login
                    + "\' doesn't exist in a database");
        }

        Optional<UserDAO> currentUserDAO = userService.getCurrentUserDAO();
        if (!currentUserDAO.isPresent()) {
            throw new UserDoesNotExistInDBException(ClientErrorCode.USER_NOT_EXISTS, "User doesn't exist in a database");
        }

        if (!Objects.equals(userDAO.get().getRoom().getRoomId(), currentUserDAO.get().getRoom().getRoomId())) {
            throw new RoomsMismatchException(ClientErrorCode.ROOMS_MISMATCH, "\'"
                    + userDAO.get().getLogin() + "\' and \'"
                    + currentUserDAO.get().getLogin() + "\' are in different rooms");
        }

        if (!currentUserDAO.get().getRoom().getGamePhase().equals(GamePhaseEnum.MAFIA_PHASE)) {
            throw new IllegalGamePhaseException(ClientErrorCode.WRONG_GAME_PHASE, "Wrong game phase");
        }

        if (!currentUserDAO.get().getCharacter().equals(org.dreamteam.mafia.dao.enums.CharacterEnum.SHERIFF)) {
            throw new NotEnoughRightsException("Permission denied");
        }

        return userDAO.get().getCharacter().equals(org.dreamteam.mafia.dao.enums.CharacterEnum.DON) ||
                userDAO.get().getCharacter().equals(org.dreamteam.mafia.dao.enums.CharacterEnum.MAFIA);
    }

    private void setRolesToUsers(RoomDAO room) {
        ArrayList<CharacterEnum> roles = new ArrayList<>();

        int mafiaAmount = room.getMafia();
        if (room.getDon()) {
            roles.add(CharacterEnum.DON);
            mafiaAmount--;
        }
        for (int i = 0; i < mafiaAmount; i++) {
            roles.add(CharacterEnum.MAFIA);
        }
        int civilianAmount = room.getMaxUsersAmount() - room.getMafia();
        if (room.getSheriff()) {
            roles.add(CharacterEnum.SHERIFF);
            civilianAmount--;
        }
        for (int i = 0; i < civilianAmount; i++) {
            roles.add(CharacterEnum.CITIZEN);
        }
        Collections.shuffle(roles);
        int roleIndex = 0;
        for (UserDAO user : room.getUserList()) {
            user.setCharacter(roles.get(roleIndex));
            roleIndex++;
        }
    }

}
