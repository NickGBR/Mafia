package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.dto.JoinRoomDTO;
import org.dreamteam.mafia.dto.RoomCreationDTO;
import org.dreamteam.mafia.dto.RoomDisplayDTO;
import org.dreamteam.mafia.dto.UserDisplayDTO;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.service.api.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для системы комнат
 */
@RestController
@RequestMapping("/api/room")
public class RoomController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Обрабатывает запросы на создание комнаты
     *
     * @param dto - описание создаваемой комнаты
     * @throws ClientErrorException - если текущий пользователь уже находится в комнате
     */
    @PostMapping("/create")
    public void createRoom(@RequestBody RoomCreationDTO dto) throws ClientErrorException {
        logger.debug("Incoming room creation request. DTO: " + dto);
        roomService.createRoom(dto);
    }

    /**
     * Обрабатывает запросы на расформирование комнаты
     *
     * @throws ClientErrorException - если текущий пользователь не находится в комнате
     *                              или если текущий пользователь не являяется администратором комнаты
     */
    @PostMapping("/disband")
    public void disbandRoom() throws ClientErrorException {
        logger.debug("Incoming room disbandment request.");
        roomService.disbandRoom();
    }

    /**
     * Обрабатывает запрос на получение списка доступных на данный момент комнат
     *
     * @return - список описаний доступных в данный момент комнат
     */
    @GetMapping("/getInitialList")
    public List<RoomDisplayDTO> getRooms() {
        logger.debug("Incoming request for available rooms");
        return roomService.getAvailableRooms();
    }

    /**
     * Обрабатывает запрос на вход в комнату
     *
     * @param dto - запрос на вход в виде идентификатора комнаты и пароля
     * @throws ClientErrorException - если комната не существует
     *                              или в ней уже началась игра
     *                              или она уже заполнена
     *                              или текущий пользователь уже в другой комнате
     *                              или пользователь дал неподходящий к комнате пароль
     */
    @PostMapping("/join")
    public void joinRoom(@RequestBody JoinRoomDTO dto) throws ClientErrorException {
        logger.debug("Incoming room join request. DTO: " + dto);
        roomService.joinRoom(dto);
    }

    /**
     * Обрабатывает запрос на выход из комнаты
     *
     * @throws ClientErrorException - если текущий пользователь не находится в комнате
     *                              или игра уже началась
     */
    @PostMapping("/leave")
    public void leaveRoom() throws ClientErrorException {
        logger.debug("Incoming room leave request.");
        if (roomService.isCurrentUserAdmin()) {
            logger.debug("Current user is room admin. Redirecting to disbandment procedure.");
            disbandRoom();
        } else {
            roomService.leaveRoom();
        }
    }

    /**
     * Обрабатывает запрос на получение списка пользователей в текущей комнате
     *
     * @return - список пользователей в текущей комнате в формате: имя, готовность и статус администратора
     * @throws ClientErrorException - если текущий пользователь не находится в комнате
     */
    @GetMapping("/getUsersList")
    public List<UserDisplayDTO> getUsersInRoom() throws ClientErrorException {
        logger.debug("Incoming request for users in the room");
        return roomService.getUsersInRoom();
    }

    /**
     * Обрабатывает запрос на смену статуса готовности текущего пользователя
     *
     * @param ready - устанавливаемый статус готовности
     * @throws ClientErrorException - если данный пользователь не находится сейчас в комнате
     */
    @PostMapping("/setReady")
    public void setReady(@RequestBody Boolean ready) throws ClientErrorException {
        logger.debug("Incoming readiness update. New value: " + ready);
        roomService.setReady(ready);
    }

    /**
     * Обрабатывает запрос на изгнание целевого пользователя из комнаты текущего пользователя
     *
     * @param target - имя изгоняемого пользователя
     * @throws ClientErrorException - если текущий пользователь не находится в комнате
     *                              или если игра в комнате уже начата
     *                              или оба пользователя не находятся в одной и той же комнате
     *                              или если запросивший пользователь не является администратором своей комнаты
     */
    @PostMapping("/kick")
    public void kickUser(@RequestBody String target) throws ClientErrorException {
        logger.debug("Incoming kick request. Target user login: " + target);
        roomService.kickUser(target);
    }
}
