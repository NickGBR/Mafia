package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dto.RoomDTO;
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

import java.util.List;
import java.util.Optional;

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
    public Room getRoomFromDTO(RoomDTO roomDTO) throws NoSuchRoomException {
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
    public void createRoom(RoomDTO roomDTO) throws AlreadyInRoomException {
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
            dao.setAdmin(admin.get());
            dao.addUser(admin.get());
            admin.get().setRoom(dao);
        }
        repository.save(dao);
    }

    @Override
    public boolean isRoomPrivate(Room room) {
        return false;
    }

    @Override
    public boolean joinRoom(User user, Room room, String roomPassword) {
        return false;
    }

    @Override
    public boolean joinRoom(User user, Room room) {
        return false;
    }

    @Override
    public List<Room> getNonFullRooms() {
        return null;
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
