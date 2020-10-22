package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.dto.InitDTO;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.service.api.RoomService;
import org.dreamteam.mafia.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/init")
public class InitController {

    private final UserService userService;
    private final RoomService roomService;

    @Autowired
    public InitController(UserService userService, RoomService roomService) {
        this.userService = userService;
        this.roomService = roomService;
    }

    @GetMapping
    public InitDTO getUserState() {
        InitDTO dto = new InitDTO();
        final Optional<User> user = userService.getCurrentUser();
        dto.setIsLoggedIn(user.isPresent());
        if (user.isPresent()) {
            dto.setName(user.get().getName());
            dto.setIsInRoom(roomService.isCurrentlyInRoom());
        }
        return dto;
    }
}
