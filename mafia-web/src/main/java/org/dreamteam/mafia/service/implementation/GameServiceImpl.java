package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dto.CharacterDisplayDTO;
import org.dreamteam.mafia.entities.RoomEntity;
import org.dreamteam.mafia.entities.UserEntity;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.model.*;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.service.api.GameService;
import org.dreamteam.mafia.service.api.MessageService;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.service.implementation.engine.GameHost;
import org.dreamteam.mafia.service.implementation.engine.GamePhaseDurationsService;
import org.dreamteam.mafia.util.ClientErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("GameService")
public class GameServiceImpl implements GameService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final MessageService messageService;
    private final UserService userService;
    private final GamePhaseDurationsService durationsService;

    private final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    @Autowired
    public GameServiceImpl(
            UserRepository userRepository,
            RoomRepository roomRepository,
            MessageService messageService,
            UserService userService,
            GamePhaseDurationsService durationsService) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.messageService = messageService;
        this.userService = userService;
        this.durationsService = durationsService;
    }

    @Override
    public void startGame() throws ClientErrorException {

        Long roomId = userService.getCurrentUserDAO().get().getRoom().getRoomId();

        Optional<UserEntity> currentUserDAO = userService.getCurrentUserDAO();
        if (!currentUserDAO.isPresent()) {
            throw new SecurityException("User doesn't exist in a database");
        }
        if (currentUserDAO.get().getRoom() == null) {
            throw new ClientErrorException(ClientErrorCode.NOT_IN_ROOM, "User is not im the room");
        }
        RoomEntity room = currentUserDAO.get().getRoom();
        if (!currentUserDAO.get().getIsAdmin()) {
            throw new ClientErrorException((ClientErrorCode.NOT_ENOUGH_RIGHTS), "User isn't admin");
        }
        room.setGameStatus(GameStatusEnum.IN_PROGRESS);
        setRolesToUsers(room);
        roomRepository.save(room);

        GameHost gameHost = new GameHost(room, roomRepository,
                                         messageService, this, durationsService);
        Thread thread = new Thread(gameHost);
        thread.start();
    }

    @Override
    public boolean isSheriff(String login) throws ClientErrorException {
        Optional<UserEntity> userDAO = userRepository.findByLogin(login);
        if (!userDAO.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.USER_NOT_EXISTS, "User \'" + login
                    + "\' doesn't exist in a database");
        }

        Optional<UserEntity> currentUserDAO = userService.getCurrentUserDAO();
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

        if (!currentUserDAO.get().getCharacter().equals(CharacterEnum.DON)) {
            throw new ClientErrorException(ClientErrorCode.NOT_ENOUGH_RIGHTS, "Permission denied");
        }

        return userDAO.get().getCharacter().equals(CharacterEnum.SHERIFF);
    }

    @Override
    public boolean isMafia(String login) throws ClientErrorException {
        Optional<UserEntity> userDAO = userRepository.findByLogin(login);
        if (!userDAO.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.USER_NOT_EXISTS, "User \'" + login
                    + "\' doesn't exist in a database");
        }

        Optional<UserEntity> currentUserDAO = userService.getCurrentUserDAO();
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

        if (!currentUserDAO.get().getCharacter().equals(CharacterEnum.SHERIFF)) {
            throw new ClientErrorException(ClientErrorCode.NOT_ENOUGH_RIGHTS, "Permission denied");
        }

        return userDAO.get().getCharacter().equals(CharacterEnum.DON) ||
                userDAO.get().getCharacter().equals(CharacterEnum.MAFIA);
    }

    @Override
    public void countVotesAgainst(String login) throws ClientErrorException {
        Optional<UserEntity> userDAO = userRepository.findByLogin(login);
        if (!userDAO.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.USER_NOT_EXISTS, "User \'" + login
                    + "\' doesn't exist in a database");
        }

        Optional<UserEntity> currentUserDAO = userService.getCurrentUserDAO();
        if (!currentUserDAO.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.USER_NOT_EXISTS, "User doesn't exist in a database");
        }

        if (!Objects.equals(userDAO.get().getRoom().getRoomId(), currentUserDAO.get().getRoom().getRoomId())) {
            throw new ClientErrorException(ClientErrorCode.ROOMS_MISMATCH, "\'"
                    + userDAO.get().getLogin() + "\' and \'"
                    + currentUserDAO.get().getLogin() + "\' are in different rooms");
        }

        if (userDAO.get().getCharacterStatus().equals(CharacterStatusEnum.DEAD) ||
                currentUserDAO.get().getCharacterStatus().equals(CharacterStatusEnum.DEAD)) {
            throw new ClientErrorException(ClientErrorCode.CHARACTER_IS_DEAD, "Character is out of game!");
        }

        if (!userDAO.get().getRoom().getGamePhase().equals(GamePhaseEnum.CIVILIANS_VOTE_PHASE)
                && !userDAO.get().getRoom().getGamePhase().equals(GamePhaseEnum.MAFIA_VOTE_PHASE)) {
            throw new ClientErrorException(ClientErrorCode.WRONG_GAME_PHASE, "Can't vote outside voting size");
        }

        if (userDAO.get().getRoom().getGamePhase().equals(GamePhaseEnum.MAFIA_VOTE_PHASE)) {
            if (!userDAO.get().getCharacter().equals(CharacterEnum.DON)
                    && !userDAO.get().getCharacter().equals(CharacterEnum.MAFIA)) {
                throw new ClientErrorException(ClientErrorCode.NOT_ENOUGH_RIGHTS,
                                               "Non-mafia can't vote in mafia voting phase");
            }
        }

        if (currentUserDAO.get().getHasVoted()) {
            throw new ClientErrorException(ClientErrorCode.USER_ALREADY_VOTED, "User doesn't exist in a database");
        }

        currentUserDAO.get().setHasVoted(true);

        Integer votesAgainst = userDAO.get().getVotesAgainst();
        userDAO.get().setVotesAgainst(votesAgainst + 1);

        userRepository.save(userDAO.get());
        userRepository.save(currentUserDAO.get());
    }

    @Override
    public void setRolesToUsers(RoomEntity room) {
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
        for (UserEntity user : room.getUserList()) {
            user.setCharacter(roles.get(roleIndex));
            roleIndex++;
        }
    }

    @Override
    public List<CharacterDisplayDTO> getCharacterInGame() throws ClientErrorException {
        Optional<UserEntity> currentUserDAO = userService.getCurrentUserDAO();
        if (!currentUserDAO.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.USER_NOT_EXISTS, "User doesn't exist in a database");
        }
        if (currentUserDAO.get().getRoom() == null) {
            throw new ClientErrorException(ClientErrorCode.NOT_IN_ROOM, "Can't request characters outside of room");
        }
        if (!(currentUserDAO.get().getRoom().getGameStatus().equals(GameStatusEnum.IN_PROGRESS) ||
                currentUserDAO.get().getRoom().getGameStatus().equals(GameStatusEnum.COMPLETED))) {
            throw new ClientErrorException(ClientErrorCode.GAME_NOT_STARTED,
                                           "Can't request characters from room without game");
        }
        List<CharacterDisplayDTO> dtoList = new ArrayList<>();
        boolean allowedToSeeMafias = currentUserDAO.get().getCharacter().equals(CharacterEnum.DON)
                || currentUserDAO.get().getCharacter().equals(CharacterEnum.MAFIA);
        currentUserDAO.get().getRoom().getUserList().stream()
                .map((dao) -> {
                    CharacterDisplayDTO dto = new CharacterDisplayDTO();
                    dto.setName(dao.getLogin());
                    dto.setIsAlive(dao.getCharacterStatus().equals(CharacterStatusEnum.ALIVE));
                    if (allowedToSeeMafias &&
                            (dao.getCharacter().equals(CharacterEnum.MAFIA) ||
                                    dao.getCharacter().equals(CharacterEnum.DON))) {
                        dto.setRole(dao.getCharacter());
                    } else {
                        dto.setRole(CharacterEnum.CITIZEN);
                    }
                    return dto;
                })
                .sorted(Comparator.comparing(CharacterDisplayDTO::getName))
                .collect(Collectors.toCollection(() -> dtoList));
        return dtoList;
    }

    public GameEndStatus isMafiaVictoryInRoom(RoomEntity room) {
        List<CharacterEnum> mafiaRoles = new ArrayList<>();
        mafiaRoles.add(CharacterEnum.DON);
        mafiaRoles.add(CharacterEnum.MAFIA);
        List<CharacterEnum> civilianRoles = new ArrayList<>();
        civilianRoles.add(CharacterEnum.CITIZEN);
        civilianRoles.add(CharacterEnum.SHERIFF);
        Long mafiaCount = userRepository.countDistinctByCharacterInAndCharacterStatusAndRoom(
                mafiaRoles, CharacterStatusEnum.ALIVE, room
        );
        Long civilianCount = userRepository.countDistinctByCharacterInAndCharacterStatusAndRoom(
                civilianRoles, CharacterStatusEnum.ALIVE, room
        );

        logger.trace("CIVILIAN " + civilianCount + "  " + "Mafia " + mafiaCount);

        if (civilianCount <= mafiaCount) {
            return GameEndStatus.MAFIA_WON;
        } else if (mafiaCount == 0) {
            return GameEndStatus.CIVILIANS_WON;
        }
        return GameEndStatus.GAME_NOT_ENDED;
    }

    @Override
    public CharacterEnum getRole() throws ClientErrorException {
        Optional<UserEntity> currentUserDAO = userService.getCurrentUserDAO();
        if (!currentUserDAO.isPresent()) {
            throw new SecurityException("User doesn't exist in a database");
        }
        logger.trace("Determining role of user: " + currentUserDAO.get().getLogin());
        if (currentUserDAO.get().getRoom() == null) {
            throw new ClientErrorException(ClientErrorCode.NOT_IN_ROOM, "User is not im the room");
        }
        if (currentUserDAO.get().getRoom().getGameStatus().equals(GameStatusEnum.NOT_STARTED) ||
                (currentUserDAO.get().getRoom().getGameStatus().equals(GameStatusEnum.DELETED))) {
            throw new ClientErrorException(ClientErrorCode.GAME_NOT_STARTED, "Game has not started in current room");
        }
        return currentUserDAO.get().getCharacter();
    }
}
