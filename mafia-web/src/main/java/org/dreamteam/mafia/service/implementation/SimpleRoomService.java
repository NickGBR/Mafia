package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;
import org.dreamteam.mafia.dto.JoinRoomDTO;
import org.dreamteam.mafia.dto.RoomCreationDTO;
import org.dreamteam.mafia.dto.RoomDisplayDTO;
import org.dreamteam.mafia.dto.UserDisplayDTO;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.dreamteam.mafia.service.api.RoomService;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.util.ClientErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Стандартная реализации сервиса комнат
 */
@Service
public class SimpleRoomService implements RoomService {

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final RoomRepository repository;

    @Autowired
    public SimpleRoomService(
            UserService userService, PasswordEncoder encoder,
            RoomRepository repository) {
        this.userService = userService;
        this.encoder = encoder;
        this.repository = repository;
    }

    @Override
    public boolean isCurrentUserAdmin() {
        Optional<UserDAO> user = userService.getCurrentUserDAO();
        if (!user.isPresent()) {
            return false;
        }
        Optional<RoomDAO> room = repository.findRoomDAOByUserListContains(user.get());
        return room.isPresent() && user.get().getIsAdmin();
    }

    @Override
    public void createRoom(RoomCreationDTO roomDTO) throws ClientErrorException {
        String name = roomDTO.getName().trim();
        if (name.length() == 0 || name.length() > 20) {
            throw new ClientErrorException(ClientErrorCode.INVALID_NAME, "Provided name is invalid");
        }
        if (roomDTO.getMafia() > (roomDTO.getMaxPlayers() / 2 + (roomDTO.getMaxPlayers() % 2 - 1))) {
            throw new ClientErrorException(ClientErrorCode.INVALID_ROOM_PARAMETERS,
                                           "Too many mafia - game would end immediately");
        }
        RoomDAO dao = new RoomDAO();
        dao.setName(name);
        if (roomDTO.getPassword().isEmpty()) {
            dao.setPasswordHash("");
        } else {
            dao.setPasswordHash(encoder.encode(roomDTO.getPassword()));
        }
        dao.setDescription(roomDTO.getDescription());
        dao.setMaxUsersAmount(roomDTO.getMaxPlayers());
        dao.setMafia(roomDTO.getMafia());
        dao.setDon(roomDTO.isDon());
        dao.setSheriff(roomDTO.isSheriff());
        Optional<UserDAO> admin = userService.getCurrentUserDAO();
        if (!admin.isPresent()) {
            throw new SecurityException("Non authorised user is not allowed to create rooms");
        }
        Optional<RoomDAO> adminCurrRoom = repository.findRoomDAOByUserListContains(admin.get());
        if (adminCurrRoom.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.ALREADY_IN_ROOM,
                                           "Can't create a room for a user, that is already in a room");
        }
        dao.addUser(admin.get());
        admin.get().setRoom(dao);
        admin.get().setIsAdmin(true);

        repository.save(dao);
    }

    public void disbandRoom() throws ClientErrorException {
        Optional<UserDAO> admin = userService.getCurrentUserDAO();
        if (!admin.isPresent()) {
            throw new SecurityException("Non authorised user is not allowed to disband rooms");
        }
        Optional<RoomDAO> adminCurrRoom = repository.findRoomDAOByUserListContains(admin.get());
        if (!adminCurrRoom.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.NOT_IN_ROOM, "Current user is not in a room");
        }
        if (!adminCurrRoom.get().getGameStatus().equals(GameStatusEnum.NOT_STARTED)) {
            throw new ClientErrorException(ClientErrorCode.GAME_ALREADY_STARTED,
                                           "Can't disband already started game");
        }
        if (!admin.get().getIsAdmin()) {
            throw new ClientErrorException(ClientErrorCode.NOT_ENOUGH_RIGHTS,
                                           "Only room administrator can disband it");
        }
        for (UserDAO user : adminCurrRoom.get().getUserList()) {
            user.setRoom(null);
        }
        adminCurrRoom.get().getUserList().clear();
        admin.get().setIsAdmin(false);
        admin.get().setIsReady(false);
        adminCurrRoom.get().setGameStatus(GameStatusEnum.DELETED);
        repository.save(adminCurrRoom.get());
    }

    @Override
    public void joinRoom(JoinRoomDTO dto) throws ClientErrorException {
        Optional<UserDAO> user = userService.getCurrentUserDAO();
        if (!user.isPresent()) {
            throw new SecurityException("Non authorised user is not allowed to join rooms");
        }
        Optional<RoomDAO> room = repository.findById(dto.getId());
        if (!room.isPresent() || room.get().getGameStatus().equals(GameStatusEnum.DELETED)) {
            throw new ClientErrorException(ClientErrorCode.ROOM_NOT_EXIST, "Requested room does not exist");
        }
        if (!room.get().getGameStatus().equals(GameStatusEnum.NOT_STARTED)) {
            throw new ClientErrorException(ClientErrorCode.GAME_ALREADY_STARTED,
                                           "Can't join already started game");
        }
        Optional<RoomDAO> userCurrRoom = repository.findRoomDAOByUserListContains(user.get());
        if (userCurrRoom.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.ALREADY_IN_ROOM,
                                           "Current user is already in a room");
        }
        if (room.get().getUserList().size() >= room.get().getMaxUsersAmount()) {
            throw new ClientErrorException(ClientErrorCode.ROOM_IS_FULL,
                                           "Impossible to join a full room");
        }
        if (!room.get().getPasswordHash().isEmpty() &&
                !encoder.matches(dto.getPassword(), room.get().getPasswordHash())) {
            throw new ClientErrorException(ClientErrorCode.INCORRECT_PASSWORD,
                                           "Provided wrong password for requested room");
        }
        room.get().addUser(user.get());
        user.get().setRoom(room.get());
        repository.save(room.get());
    }

    @Override
    public void leaveRoom() throws ClientErrorException {
        Optional<UserDAO> user = userService.getCurrentUserDAO();
        if (!user.isPresent()) {
            throw new SecurityException("Non authorised user is not allowed to join rooms");
        }
        Optional<RoomDAO> room = repository.findRoomDAOByUserListContains(user.get());
        if (!room.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.NOT_IN_ROOM,
                                           "User is not in room. Nothing to leave from");
        }
        if (room.get().getGameStatus().equals(GameStatusEnum.IN_PROGRESS)) {
            throw new ClientErrorException(ClientErrorCode.GAME_ALREADY_STARTED,
                                           "Can't leave while game is in progress");
        }
        room.get().removeUser(user.get());
        user.get().setRoom(null);
        user.get().setIsReady(false);
        repository.save(room.get());
    }

    @Override
    public List<RoomDisplayDTO> getAvailableRooms() {
        List<RoomDAO> availableRooms = repository.findRoomDAOByGameStatus(GameStatusEnum.NOT_STARTED);
        List<RoomDisplayDTO> dtoRooms = new ArrayList<>();
        availableRooms.stream().map(RoomDisplayDTO::new)
                .collect(Collectors.toCollection(() -> dtoRooms));
        return dtoRooms;
    }

    @Override
    public List<UserDisplayDTO> getUsersInRoom() throws ClientErrorException {
        final Optional<UserDAO> user = userService.getCurrentUserDAO();
        if (!user.isPresent()) {
            throw new SecurityException("Non authorised user is not allowed to get users in room");
        }
        Optional<RoomDAO> room = repository.findRoomDAOByUserListContains(user.get());
        if (!room.isPresent() || room.get().getGameStatus().equals(GameStatusEnum.DELETED)) {
            throw new ClientErrorException(ClientErrorCode.NOT_IN_ROOM,
                                           "User is not in room. Nothing to list");
        }
        List<UserDisplayDTO> dtoList = new ArrayList<>();
        room.get().getUserList().stream()
                .map((dao) -> {
                    UserDisplayDTO dto = new UserDisplayDTO();
                    dto.setName(dao.getLogin());
                    dto.setAdmin(dao.getIsAdmin());
                    dto.setReady(dao.getIsReady());
                    return dto;
                })
                .sorted(Comparator.comparing(UserDisplayDTO::getName))
                .collect(Collectors.toCollection(() -> dtoList));
        return dtoList;
    }

    @Override
    public void kickUser(String target) throws ClientErrorException {
        Optional<UserDAO> admin = userService.getCurrentUserDAO();
        if (!admin.isPresent()) {
            throw new SecurityException("Non authorised user is not allowed to kick others out of rooms");
        }
        Optional<RoomDAO> adminCurrRoom = repository.findRoomDAOByUserListContains(admin.get());
        if (!adminCurrRoom.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.NOT_IN_ROOM, "Current user is not in a room");
        }
        if (!adminCurrRoom.get().getGameStatus().equals(GameStatusEnum.NOT_STARTED)) {
            throw new ClientErrorException(ClientErrorCode.GAME_ALREADY_STARTED,
                                           "Can't kick from an already started game");
        }
        if (!admin.get().getIsAdmin()) {
            throw new ClientErrorException(ClientErrorCode.NOT_ENOUGH_RIGHTS,
                                           "Only room administrator can kick users");
        }
        Optional<UserDAO> targetUser = adminCurrRoom.get().getUserList().stream()
                .filter((user) -> user.getLogin().equals(target)).findFirst();
        if (!targetUser.isPresent()) {
            throw new ClientErrorException(ClientErrorCode.ROOMS_MISMATCH, "Target is not in the same room");
        }
        targetUser.get().setRoom(null);
        targetUser.get().setIsReady(false);
        adminCurrRoom.get().removeUser(targetUser.get());
        repository.save(adminCurrRoom.get());
    }


    @Override
    public void setReady(boolean ready) throws ClientErrorException {
        final Optional<UserDAO> user = userService.getCurrentUserDAO();
        if (!user.isPresent()) {
            throw new SecurityException("Non authorised user is not allowed to change readiness");
        }
        Optional<RoomDAO> room = repository.findRoomDAOByUserListContains(user.get());
        if (!room.isPresent() || room.get().getGameStatus().equals(GameStatusEnum.DELETED)) {
            throw new ClientErrorException(ClientErrorCode.NOT_IN_ROOM,
                                           "User is not in room. Can't change readiness");
        }
        user.get().setIsReady(ready);
        repository.save(room.get());
    }

    @Override
    public boolean isRoomReady() throws ClientErrorException {
        final Optional<UserDAO> user = userService.getCurrentUserDAO();
        if (!user.isPresent()) {
            throw new SecurityException("Non authorised user is not allowed to change readiness");
        }
        Optional<RoomDAO> room = repository.findRoomDAOByUserListContains(user.get());
        if (!room.isPresent() || room.get().getGameStatus().equals(GameStatusEnum.DELETED)) {
            throw new ClientErrorException(ClientErrorCode.NOT_IN_ROOM,
                                           "User is not in room. Can't check room readiness");
        }
        final long count = room.get().getUserList().stream()
                .map(UserDAO::getIsReady)
                .filter((ready) -> ready)
                .count();
        return count == (room.get().getMaxUsersAmount() - 1);
    }

    @Override
    public boolean isCurrentlyInRoom() {
        final Optional<UserDAO> user = userService.getCurrentUserDAO();
        if (!user.isPresent()) {
            return false;
        }
        Optional<RoomDAO> room = repository.findRoomDAOByUserListContains(user.get());
        return room.isPresent();
    }

    @Override
    public RoomDisplayDTO getCurrentRoom() throws ClientErrorException {
        return new RoomDisplayDTO(getCurrentRoomDAO());
    }

    @Override
    public RoomDAO getCurrentRoomDAO() throws ClientErrorException {
        final Optional<UserDAO> user = userService.getCurrentUserDAO();
        if (!user.isPresent()) {
            throw new SecurityException("Non authorised user is not allowed to get its room ID");
        }
        Optional<RoomDAO> room = repository.findRoomDAOByUserListContains(user.get());
        if (!room.isPresent() || room.get().getGameStatus().equals(GameStatusEnum.DELETED)) {
            throw new ClientErrorException(ClientErrorCode.NOT_IN_ROOM,
                                           "User is not in room. Can't get ID");
        }
        return room.get();
    }
}
