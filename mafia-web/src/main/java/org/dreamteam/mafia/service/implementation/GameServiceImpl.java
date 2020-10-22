package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dao.enums.CharacterEnum;
import org.dreamteam.mafia.dao.enums.GamePhaseEnum;
import org.dreamteam.mafia.dto.CharacterDTO;
import org.dreamteam.mafia.exceptions.*;
import org.dreamteam.mafia.model.Character;
import org.dreamteam.mafia.model.Message;
import org.dreamteam.mafia.model.Room;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.repository.api.MessageRepository;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.service.api.GameService;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.util.ClientErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("gameService")
public class GameServiceImpl implements GameService {

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

    /**
     * Находит игру, идущую в указанной комнате
     *
     * @param room - комната
     * @return - игра, идущая в комнате
     * @throws GameNotStartedException - если игра в комнате еще не началась
     */
    @Override
    public Room getGameInRoom(Room room) throws GameNotStartedException {
        return null;
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
     * @throws GameIsOverException - если игра уже окончена
     */
    @Override
    public void advancePhase(Room room) throws GameIsOverException {

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

        if (!currentUserDAO.get().getRoom().getGamePhase().equals(GamePhaseEnum.NIGHT)) {
            throw new IllegalGamePhaseException(ClientErrorCode.WRONG_GAME_PHASE, "Wrong game phase");
        }

        if (!currentUserDAO.get().getCharacter().equals(CharacterEnum.DON)) {
            throw new NotEnoughRightsException("Permission denied");
        }

        return userDAO.get().getCharacter().equals(CharacterEnum.SHERIFF);

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

        if (!currentUserDAO.get().getRoom().getGamePhase().equals(GamePhaseEnum.NIGHT)) {
            throw new IllegalGamePhaseException(ClientErrorCode.WRONG_GAME_PHASE, "Wrong game phase");
        }

        if (!currentUserDAO.get().getCharacter().equals(CharacterEnum.SHERIFF)) {
            throw new NotEnoughRightsException("Permission denied");
        }

        return userDAO.get().getCharacter().equals(CharacterEnum.DON) ||
                userDAO.get().getCharacter().equals(CharacterEnum.MAFIA);
    }

}
