package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.dto.RoomDTO;
import org.dreamteam.mafia.exceptions.AlreadyInRoomException;
import org.dreamteam.mafia.service.api.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void createRoom(@RequestBody RoomDTO room) throws AlreadyInRoomException {
        roomService.createRoom(room);
    }
}
