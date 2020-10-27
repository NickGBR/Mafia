package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.constants.SockConst;
import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dao.enums.CharacterEnum;
import org.dreamteam.mafia.dao.enums.CharacterStatusEnum;
import org.dreamteam.mafia.dao.enums.GamePhaseEnum;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;
import org.dreamteam.mafia.exceptions.*;
import org.dreamteam.mafia.repository.api.MessageRepository;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.service.api.GameService;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.service.implementation.GameEngine.GameHost;
import org.dreamteam.mafia.util.ClientErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Service("GameService")
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

        GameHost gameHost = new GameHost(messagingTemplate, currentUserDAO.get().getRoom(), roomRepository);
        Thread thread = new Thread(gameHost);
        thread.start();
    }

    @Override
    public boolean isSheriff(String login) throws ClientErrorException {
        Optional<UserDAO> userDAO = userRepository.findByLogin(login);
        if (!userDAO.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.USER_NOT_EXISTS, "User \'" + login
                    + "\' doesn't exist in a database" );
        }

        Optional<UserDAO> currentUserDAO = userService.getCurrentUserDAO();
        if (!currentUserDAO.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.USER_NOT_EXISTS, "User doesn't exist in a database");
        }

        if (!Objects.equals(userDAO.get().getRoom().getRoomId(), currentUserDAO.get().getRoom().getRoomId())) {
            throw new ClientErrorException(ClientErrorCode.ROOMS_MISMATCH, "\'"
                    + userDAO.get().getLogin() + "\' and \'"
                    + currentUserDAO.get().getLogin() + "\' are in different rooms");
        }

        if (!currentUserDAO.get().getRoom().getGamePhase().equals(GamePhaseEnum.DON_PHASE)) {
            throw new ClientErrorException(ClientErrorCode.WRONG_GAME_PHASE, "Wrong game phase");
        }

        if (!currentUserDAO.get().getCharacter().equals(org.dreamteam.mafia.dao.enums.CharacterEnum.DON)) {
            throw new ClientErrorException(ClientErrorCode.NOT_ENOUGH_RIGHTS, "Permission denied");
        }

        return userDAO.get().getCharacter().equals(org.dreamteam.mafia.dao.enums.CharacterEnum.SHERIFF);

    }

    @Override
    public boolean isMafia(String login) throws ClientErrorException {
        Optional<UserDAO> userDAO = userRepository.findByLogin(login);
        if (!userDAO.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.USER_NOT_EXISTS, "User \'" + login
                    + "\' doesn't exist in a database");
        }

        Optional<UserDAO> currentUserDAO = userService.getCurrentUserDAO();
        if (!currentUserDAO.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.USER_NOT_EXISTS, "User doesn't exist in a database");
        }

        if (!Objects.equals(userDAO.get().getRoom().getRoomId(), currentUserDAO.get().getRoom().getRoomId())) {
            throw new ClientErrorException(ClientErrorCode.ROOMS_MISMATCH, "\'"
                    + userDAO.get().getLogin() + "\' and \'"
                    + currentUserDAO.get().getLogin() + "\' are in different rooms");
        }

        if (!currentUserDAO.get().getRoom().getGamePhase().equals(GamePhaseEnum.SHERIFF_PHASE)) {
            throw new ClientErrorException(ClientErrorCode.WRONG_GAME_PHASE, "Wrong game phase");
        }

        if (!currentUserDAO.get().getCharacter().equals(org.dreamteam.mafia.dao.enums.CharacterEnum.SHERIFF)) {
            throw new ClientErrorException(ClientErrorCode.NOT_ENOUGH_RIGHTS, "Permission denied");
        }

        return userDAO.get().getCharacter().equals(org.dreamteam.mafia.dao.enums.CharacterEnum.DON) ||
                userDAO.get().getCharacter().equals(org.dreamteam.mafia.dao.enums.CharacterEnum.MAFIA);
    }

    @Override
    public void countVotesAgainst(String login) throws ClientErrorException {
        Optional<UserDAO> userDAO = userRepository.findByLogin(login);
        if (!userDAO.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.USER_NOT_EXISTS, "User \'" + login
                    + "\' doesn't exist in a database");
        }

        Optional<UserDAO> currentUserDAO = userService.getCurrentUserDAO();
        if (!currentUserDAO.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.USER_NOT_EXISTS, "User doesn't exist in a database");
        }

        if (!Objects.equals(userDAO.get().getRoom().getRoomId(), currentUserDAO.get().getRoom().getRoomId())) {
            throw new ClientErrorException(ClientErrorCode.ROOMS_MISMATCH, "\'"
                    + userDAO.get().getLogin() + "\' and \'"
                    + currentUserDAO.get().getLogin() + "\' are in different rooms");
        }

        if (!currentUserDAO.get().getRoom().getGamePhase().equals(GamePhaseEnum.CIVILIANS_PHASE)) {
            throw new ClientErrorException(ClientErrorCode.WRONG_GAME_PHASE, "Wrong game phase");
        }

        if (userDAO.get().getCharacterStatus().equals(CharacterStatusEnum.DEAD) ||
                currentUserDAO.get().getCharacterStatus().equals(CharacterStatusEnum.DEAD)) {
            throw new ClientErrorException(ClientErrorCode.CHARACTER_IS_DEAD, "Character is out of game!");
        }

        Integer votesAgainst = userDAO.get().getVotesAgainst();
        userDAO.get().setVotesAgainst(votesAgainst + 1);
        userRepository.save(userDAO.get());

    }

    @Override
    public void setRolesToUsers(RoomDAO room) {
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
