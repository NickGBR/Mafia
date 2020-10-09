package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.model.Game;
import org.dreamteam.mafia.model.Room;
import org.dreamteam.mafia.model.User;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    /**
     *
     * @param creator - игрок, создающий комнату
     * @param name - имя комнаты
     * @param Description - описание комнаты
     * @param password - пароль для приватной комнаты или пустой Optional для публичной комнаты
     * @param playerCount - число игроков для начала игры или пустой Optional для игры с 10 игроками по умолчанию.
     * @return
     */
    Room createRoom(User creator, String name, String Description,
                    Optional<String> password, Optional<Integer> playerCount);

    boolean isRoomPrivate(Room room);

    boolean joinRoom(User user, Room room, Optional<String> roomPassword);

    List<Room> getNonFullRooms();

    List<User> getUsersInRoom(Room room);

    boolean kickUser(User admin, User target, Room room);

    boolean isRoomFull(Room room);

    void setReady(User user, Room room, boolean ready);

    boolean isRoomReady(Room room);

    Game startGame(User user, Room room);


}
