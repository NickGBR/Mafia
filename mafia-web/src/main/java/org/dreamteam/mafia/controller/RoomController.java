package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.dto.RoomCreationDTO;
import org.dreamteam.mafia.dto.RoomDisplayDTO;
import org.dreamteam.mafia.exceptions.AlreadyInRoomException;
import org.dreamteam.mafia.service.api.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для системы комнат
 */
@RestController
@RequestMapping("/api/room")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create")
    public void createRoom(@RequestBody RoomCreationDTO room) throws AlreadyInRoomException {
        roomService.createRoom(room);
    }

    @GetMapping("/getInitialList")
    public List<RoomDisplayDTO> getRooms() {
        return roomService.getAvailableRooms();
    }
}
