package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.model.Game;
import org.dreamteam.mafia.model.Room;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.util.OptionalWithMessage;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    /**
     * Создает приватную комнату с заданным число игроков
     * @param creator - игрок, создающий комнату
     * @param name - имя комнаты
     * @param Description - описание комнаты
     * @param password - пароль для приватной комнаты
     * @param playerCount - число игроков необходимое для начала игры
     * @return - созданная комната
     */
    Room createRoom(User creator, String name, String Description,
                    String password, int playerCount);

    /**
     * Создает приватную комнату с число игроков по умолчанию (10)
     * @param creator - игрок, создающий комнату
     * @param name - имя комнаты
     * @param Description - описание комнаты
     * @param password - пароль для приватной комнаты
     * @return - созданная комната
     */
    Room createRoom(User creator, String name, String Description,
                    String password);

    /**
     * Создает публичную комнату с заданным число игроков
     * @param creator - игрок, создающий комнату
     * @param name - имя комнаты
     * @param Description - описание комнаты
     * @param playerCount - число игроков необходимое для начала игры
     * @return - созданная комната
     */
    Room createRoom(User creator, String name, String Description,
                    int playerCount);

    /**
     * Создает публичную комнату  с число игроков по умолчанию (10)
     * @param creator - игрок, создающий комнату
     * @param name - имя комнаты
     * @param Description - описание комнаты
     * @return - созданная комната
     */
    Room createRoom(User creator, String name, String Description);

    /**
     * Проверяет является ли заданная комната приватной
     * @param room - комната для проверки
     * @return - true, если комната приватна, false - иначе
     */
    boolean isRoomPrivate(Room room);

    /**
     * Пытается добавить пользователя в приватную комнату
     * @param user - пользователь
     * @param room - комната
     * @param roomPassword - пароль от комнаты
     * @return -  true, если удалось добавить пользователя в комнату, false -иначе
     */
    boolean joinRoom(User user, Room room, String roomPassword);

    /**
     * Пытается добавить пользователя в публичную комнату
     * @param user - пользователь
     * @param room - комната
     * @return -  true, если удалось добавить пользователя в комнату, false -иначе
     */
    boolean joinRoom(User user, Room room);

    /**
     * Возвращает все незаполненные (доступные для присоединения) комнаты в приложении
     * @return - список незаполненных комнат
     */
    List<Room> getNonFullRooms();

    /**
     * Возвращает список пользователей внутри комнат
     * @param room - комната
     * @return - список пользователей в комнате
     */
    List<User> getUsersInRoom(Room room);

    /**
     * Пытается убрать пользователя из комнаты
     * @param admin - пользователь, запросивший изгнанине
     * @param target - изгоняемый пользователь
     * @param room - комната
     * @return - true, если изгнание было проведено успешно, false - иначе
     */
    boolean kickUser(User admin, User target, Room room);

    /**
     * Проверяет заполнена ли комната
     * @param room - комната для проверки
     * @return - true, если комната заполнена, false - иначе
     */
    boolean isRoomFull(Room room);

    /**
     * Подтверждает\отменяет готовность пользователя для начала игры в комнате
     * @param user - пользователь
     * @param room - комната
     * @param ready - состояния, на которое нужно изменить готовность
     */
    void setReady(User user, Room room, boolean ready);

    /**
     * Проверяет готовы ли все пользователи  в комнате для начала игры
     * @param room - комната для проверки
     * @return - true, если все пользователи в комнате готовы, false - иначе
     */
    boolean isRoomReady(Room room);

    /**
     *  Запускает игру в комнате
     * @param user - игрок, запускающий игру (должен быть адмнистратором комнаты)
     * @param room - комната
     * @return - созданную игру или пустой Optional и  сообщение об ошибкке
     */
    OptionalWithMessage<Game> startGame(User user, Room room);


}
