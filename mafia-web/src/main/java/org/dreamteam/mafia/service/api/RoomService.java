package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.dto.JoinRoomDTO;
import org.dreamteam.mafia.dto.RoomCreationDTO;
import org.dreamteam.mafia.dto.RoomDisplayDTO;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.model.Room;
import org.dreamteam.mafia.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс с ервиса, обслуживающего систему комнат
 */
public interface RoomService {

    /**
     * Возвращает комнату, в которой находится текущий пользователь
     *
     * @return - найденная комната или пустой Optional, если пользователь не находится в комнате
     */
    Optional<Room> getCurrentUserRoom();

    /**
     * Создает новую комнату и сохраняет ее в базу, назначая текущего пользователя
     * администратором комнаты.
     *
     * @param roomDTO - описание комнаты, полученние из интерфейса
     * @throws ClientErrorException - если текущий пользователь уже в комнате и не может создать новую
     */
    void createRoom(RoomCreationDTO roomDTO) throws ClientErrorException;

    /**
     * Распускает текущую комнату, если текущий игрок - администратор комнаты.
     *
     * @throws ClientErrorException - если текущий пользователь не находится в комнате
     *                              или если текущий пользователь не являяется администратором комнаты
     */
    void disbandRoom() throws ClientErrorException;

    /**
     * Проверяет является ли заданная комната приватной
     *
     * @param room - описание комнаты, полученние из интерфейса
     * @return - true, если комната приватна, false - иначе
     */
    boolean isRoomPrivate(Room room);

    /**
     * Пытается добавить текущего пользователя в комнату с заданным ID
     *
     * @param dto - данные, необходимые для попытки входа в комнату
     */
    void joinRoom(JoinRoomDTO dto) throws ClientErrorException;

    /**
     * Покидает текущую комнату, если текущий игрок находится в комнате
     *
     * @throws ClientErrorException - если текущий пользователь не находится в комнате
     */
    void leaveRoom() throws ClientErrorException;

    /**
     * Возвращает все незаполненные (доступные для присоединения) комнаты в приложении
     *
     * @return - список незаполненных комнат
     */
    List<RoomDisplayDTO> getAvailableRooms();

    /**
     * Возвращает список пользователей внутри комнат
     *
     * @param room - комната
     * @return - список пользователей в комнате
     */
    List<User> getUsersInRoom(Room room);

    /**
     * Пытается убрать пользователя из комнаты
     *
     * @param admin  - пользователь, запросивший изгнанине
     * @param target - изгоняемый пользователь
     * @throws ClientErrorException - если оба пользователя не находятся в одной и той же комнате
     *                              или если запросивший пользователь не является администратором своей комнаты
     */
    void kickUser(User admin, User target) throws ClientErrorException;

    /**
     * Проверяет заполнена ли комната
     *
     * @param room - комната для проверки
     * @return - true, если комната заполнена, false - иначе
     */
    boolean isRoomFull(Room room);

    /**
     * Подтверждает\отменяет готовность пользователя для начала игры в комнате, в которой он сейчас находится
     *
     * @param ready - состояния, на которое нужно изменить готовность
     * @throws ClientErrorException - если данный пользователь не находится сейчас в комнате
     */
    void setReady(boolean ready) throws ClientErrorException;

    /**
     * Проверяет готовы ли все пользователи  в комнате для начала игры
     *
     * @param room - комната для проверки
     * @return - true, если все пользователи в комнате готовы, false - иначе
     */
    boolean isRoomReady(Room room);
}
