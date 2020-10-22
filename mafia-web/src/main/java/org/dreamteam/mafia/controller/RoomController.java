package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.dto.RoomCreationDTO;
import org.dreamteam.mafia.dto.RoomDisplayDTO;
import org.dreamteam.mafia.exceptions.AlreadyInRoomException;
import org.dreamteam.mafia.exceptions.NoSuchRoomException;
import org.dreamteam.mafia.exceptions.NotEnoughRightsException;
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
     * @throws AlreadyInRoomException - если текущий пользователь уже находится в комнате
     */
    @PostMapping("/create")
    public void createRoom(@RequestBody RoomCreationDTO dto) throws AlreadyInRoomException {
        logger.debug("Incoming room creation request. DTO: " + dto);
        roomService.createRoom(dto);
    }

    /**
     * Обрабатывает запросы на расформирование комнаты
     *
     * @throws NoSuchRoomException      - если текущий пользователь не находится в комнате
     * @throws NotEnoughRightsException - если текущий пользователь не являяется администратором комнаты
     */
    @PostMapping("/disband")
    public void disbandRoom() throws NoSuchRoomException, NotEnoughRightsException {
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
}
