package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.dto.JoinRoomDTO;
import org.dreamteam.mafia.dto.RoomCreationDTO;
import org.dreamteam.mafia.dto.RoomDisplayDTO;
import org.dreamteam.mafia.dto.UserDisplayDTO;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.model.Room;

import java.util.List;

/**
 * Интерфейс с ервиса, обслуживающего систему комнат
 */
public interface RoomService {

    /**
     * Проверяет, является ли текущий пользователь администратором комнаты.
     *
     * @return - true, если текуий пользователь - администратор комнаты, false - в остальных случаях
     */
    boolean isCurrentUserAdmin();

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
     * Пытается добавить текущего пользователя в комнату с заданным ID
     *
     * @param dto - данные, необходимые для попытки входа в комнату
     * @throws ClientErrorException - если комната не существует
     *                              или в ней уже началась игра
     *                              или она уже заполнена
     *                              или текущий пользователь уже в другой комнате
     *                              или пользователь дал неподходящий к комнате пароль
     */
    void joinRoom(JoinRoomDTO dto) throws ClientErrorException;

    /**
     * Покидает текущую комнату, если текущий игрок находится в комнате
     *
     * @throws ClientErrorException - если текущий пользователь не находится в комнате
     *                              или игра уже началась
     */
    void leaveRoom() throws ClientErrorException;

    /**
     * Возвращает все незаполненные (доступные для присоединения) комнаты в приложении
     *
     * @return - список незаполненных комнат
     */
    List<RoomDisplayDTO> getAvailableRooms();

    /**
     * Возвращает список пользователей внутри комнаты текущего пользователя
     *
     * @return - список пользователей в комнате
     * @throws ClientErrorException - если текущий пользователь не находится в комнате
     */
    List<UserDisplayDTO> getUsersInRoom() throws ClientErrorException;

    /**
     * Пытается убрать пользователя из комнаты
     *
     * @param target - логин изгоняемого пользователя
     * @throws ClientErrorException - если текущий пользователь не находится в комнате
     *                              или если игра в комнате уже начата
     *                              или оба пользователя не находятся в одной и той же комнате
     *                              или если запросивший пользователь не является администратором своей комнаты
     */
    void kickUser(String target) throws ClientErrorException;

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
     * Проверяет готовы ли все пользователи  в комнате текущего пользователя для начала игры
     *
     * @return - true, если все пользователи в комнате готовы, false - иначе
     */
    boolean isRoomReady() throws ClientErrorException;

    /**
     * Проверяет находится ли текущий пользователь в комнате
     *
     * @return - true, если пользователь находится в комнате, false - в противном случае
     */
    boolean isCurrentlyInRoom();

    /**
     * Возвращает описание текущей комнаты для клиента
     *
     * @return -  описание комнаты в формате пригодном для отправки клиенту
     * @throws ClientErrorException - если пользователь не находится в комнате.
     */
    RoomDisplayDTO getCurrentRoom() throws ClientErrorException;
}
