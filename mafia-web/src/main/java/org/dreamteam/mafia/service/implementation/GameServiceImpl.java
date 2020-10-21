package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dao.enums.CharacterEnum;
import org.dreamteam.mafia.dao.enums.GamePhaseEnum;
import org.dreamteam.mafia.exceptions.IllegalGamePhaseException;
import org.dreamteam.mafia.exceptions.NotEnoughRightsException;
import org.dreamteam.mafia.exceptions.RoomsMismatchException;
import org.dreamteam.mafia.exceptions.UserDoesNotExistInDBException;
import org.dreamteam.mafia.repository.api.MessageRepository;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.util.ClientErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service("gameService")
public class GameServiceImpl {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;

    @Autowired
    public GameServiceImpl(UserRepository userRepository,
                           RoomRepository roomRepository,
                           MessageRepository messageRepository,
                           UserService userService) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    boolean isSheriff(String login) throws IllegalGamePhaseException, UserDoesNotExistInDBException, RoomsMismatchException, NotEnoughRightsException {
        Optional<UserDAO> userDAO = userRepository.findByLogin(login);
        if (!userDAO.isPresent()) {
            throw new UserDoesNotExistInDBException(ClientErrorCode.USER_NOT_EXISTS, "User \'" + login
                    + "\' doesn't exist in a database");
        }

        Optional<UserDAO> currentUserDAO = userService.getCurrentUserDAO();
        if (!currentUserDAO.isPresent()) {
            throw new UserDoesNotExistInDBException(ClientErrorCode.USER_NOT_EXISTS, "User doesn't exist");
        }

        if (!Objects.equals(userDAO.get().getRoom().getRoomId(), currentUserDAO.get().getRoom().getRoomId())) {
            throw new RoomsMismatchException(ClientErrorCode.ROOMS_MISMATCH, "\'"
                    + userDAO.get().getLogin() + "\' and \'"
                    + currentUserDAO.get().getLogin() + "\' are in different rooms");
        }

        if (!currentUserDAO.get().getRoom().getGamePhase().equals(GamePhaseEnum.NIGHT)) {
            throw new IllegalGamePhaseException(ClientErrorCode.WRONG_GAME_PHASE, "Wrong game phase");
        }

        if (!currentUserDAO.get().getCharacter().equals(CharacterEnum.DON)) {
            throw new NotEnoughRightsException("Permission denied");
        }

        return userDAO.get().getCharacter().equals(CharacterEnum.SHERIFF);

    }

/*    public boolean isSheriff(UserDAO user, RoomDAO room) throws IllegalGamePhaseException {
        if (!room.getGamePhase().equals(GamePhaseEnum.NIGHT))
            throw new IllegalGamePhaseException(ClientErrorCode.WRONG_GAME_PHASE, "Wrong game phase");
        else return user.getCharacter().equals(CharacterEnum.SHERIFF);
    }

    boolean isMafia(String login) throws IllegalGamePhaseException {

    public boolean isMafia(UserDAO user, RoomDAO room) throws IllegalGamePhaseException {
        if (!room.getGamePhase().equals(GamePhaseEnum.DAY))
          throw new IllegalGamePhaseException(ClientErrorCode.WRONG_GAME_PHASE, "Wrong game phase");
        else return user.getCharacter().equals(CharacterEnum.DON);
    }*/
}
