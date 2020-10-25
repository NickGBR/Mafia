package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;
import org.dreamteam.mafia.dto.InitDTO;
import org.dreamteam.mafia.dto.RoomDisplayDTO;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.service.api.RoomService;
import org.dreamteam.mafia.service.api.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/init")
public class InitController {

    private final Logger logger = LoggerFactory.getLogger(InitController.class);
    private final UserService userService;
    private final RoomService roomService;

    @Autowired
    public InitController(UserService userService, RoomService roomService) {
        this.userService = userService;
        this.roomService = roomService;
    }

    @GetMapping
    public InitDTO getUserState() throws ClientErrorException {

        logger.debug("Incoming request for initialisation data");
        InitDTO dto = new InitDTO();
        final Optional<User> user = userService.getCurrentUser();
        dto.setIsLoggedIn(user.isPresent());
        if (user.isPresent()) {
            dto.setName(user.get().getName());
            dto.setIsInRoom(roomService.isCurrentlyInRoom());
            if (dto.getIsInRoom()) {
                final RoomDisplayDTO currentRoom = roomService.getCurrentRoom();
                final RoomDAO roomDAO = userService.getCurrentUserDAO().get().getRoom();
                dto.setIsGameStarted(roomDAO.getGameStatus().equals(GameStatusEnum.IN_PROGRESS));
                dto.setRoomID(String.valueOf(currentRoom.getId()));
                dto.setRoomName(currentRoom.getName());
                dto.setIsAdmin(roomService.isCurrentUserAdmin());
                dto.setRole(userService.getCurrentUserDAO().get().getCharacter());
                dto.setIsReady(user.get().isReady());
                dto.setMaxUserAmount(currentRoom.getMaxPlayers());
            }
        }
        return dto;
    }
}
