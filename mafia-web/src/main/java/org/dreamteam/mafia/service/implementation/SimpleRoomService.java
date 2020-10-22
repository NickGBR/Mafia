package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;
import org.dreamteam.mafia.dto.JoinRoomDTO;
import org.dreamteam.mafia.dto.RoomCreationDTO;
import org.dreamteam.mafia.dto.RoomDisplayDTO;
import org.dreamteam.mafia.exceptions.AlreadyInRoomException;
import org.dreamteam.mafia.exceptions.NoSuchRoomException;
import org.dreamteam.mafia.exceptions.NotEnoughRightsException;
import org.dreamteam.mafia.model.Room;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.dreamteam.mafia.service.api.RoomService;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.util.ClientErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public Room getRoomFromDTO(RoomCreationDTO roomDTO) throws NoSuchRoomException {
        return null;
    }

    @Override
    public Optional<Room> getCurrentUserRoom() {
        Optional<UserDAO> user = userService.getCurrentUserDAO();
        if (!user.isPresent()) {
            return Optional.empty();
        } else {
            Optional<RoomDAO> roomDAO = repository.findRoomDAOByUserListContains(user.get());
            return roomDAO.map(Room::new);
        }
    }

    @Override
    public User getRoomAdmin(Room room) {
        return null;
    }

    @Override
    public void createRoom(RoomCreationDTO roomDTO) throws AlreadyInRoomException {
        RoomDAO dao = new RoomDAO();
        dao.setName(roomDTO.getName());
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
        } else {
            Optional<RoomDAO> adminCurrRoom = repository.findRoomDAOByUserListContains(admin.get());
            if (adminCurrRoom.isPresent()) {
                throw new AlreadyInRoomException(ClientErrorCode.ALREADY_IN_ROOM,
                                                 "Can't create a room for a user, that is already in a room");
            }
            dao.addUser(admin.get());
            admin.get().setRoom(dao);
            admin.get().setIsAdmin(true);
        }
        repository.save(dao);
    }

    public void disbandRoom() throws NoSuchRoomException {
        Optional<UserDAO> admin = userService.getCurrentUserDAO();
        if (!admin.isPresent()) {
            throw new SecurityException("Non authorised user is not allowed to disband rooms");
        } else {
            Optional<RoomDAO> adminCurrRoom = repository.findRoomDAOByUserListContains(admin.get());
            if (!adminCurrRoom.isPresent()) {
                throw new NoSuchRoomException("Current user is not a room admin");
            }
            for (UserDAO user : adminCurrRoom.get().getUserList()) {
                user.setRoom(null);
            }
            adminCurrRoom.get().getUserList().clear();
            admin.get().setIsAdmin(false);
            adminCurrRoom.get().setGameStatus(GameStatusEnum.DELETED);
            repository.save(adminCurrRoom.get());
        }
    }

    @Override
    public boolean isRoomPrivate(Room room) {
        return false;
    }

    @Override
    public void joinRoom(JoinRoomDTO dto) {
    }

    @Override
    public List<RoomDisplayDTO> getAvailableRooms() {
        List<RoomDAO> availableRooms = repository.findRoomDAOByGameStatus(GameStatusEnum.NOT_STARTED);
        List<RoomDisplayDTO> dtoRooms = new ArrayList<>();
        availableRooms.stream().map((dao) -> {
            RoomDisplayDTO dto = new RoomDisplayDTO();
            dto.setName(dao.getName());
            dto.setId(dao.getRoomId());
            dto.setDescription(dao.getDescription());
            dto.setMaxPlayers(dao.getMaxUsersAmount());
            dto.setPrivateRoom(!dao.getPasswordHash().equals(""));
            dto.setCurrPlayers(dao.getUserList().size());
            return dto;
        }).collect(Collectors.toCollection(() -> dtoRooms));
        return dtoRooms;
    }

    @Override
    public List<User> getUsersInRoom(Room room) {
        return null;
    }

    @Override
    public void kickUser(User admin, User target) throws NoSuchRoomException, NotEnoughRightsException {

    }

    @Override
    public boolean isRoomFull(Room room) {
        return false;
    }

    @Override
    public void setReady(User user, boolean ready) throws NoSuchRoomException {

    }

    @Override
    public boolean isRoomReady(Room room) {
        return false;
    }

    @Override
    public void startGame(User user) throws NoSuchRoomException, NotEnoughRightsException {

    }
}
