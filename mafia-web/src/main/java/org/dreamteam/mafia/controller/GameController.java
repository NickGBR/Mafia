package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.constants.SockConst;
import org.dreamteam.mafia.exceptions.*;
import org.dreamteam.mafia.service.api.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/room")
public class GameController {

    private final GameService gameService;


    @Autowired
    public GameController(GameService gameService)
    {
        this.gameService = gameService;
    }


    @RequestMapping(value = SockConst.REQUEST_GET_START_GAME_INFO, method = RequestMethod.GET)
    public void startGame() throws ClientErrorException {
        gameService.startGame();
    }

//    @RequestMapping(value = SockConst.REQUEST_GET_USER_ROLES_INFO, method = RequestMethod.GET)
//    public void getUserRoles() throws ClientErrorException {
//        gameService.startGame();
//        Set<UserDAO> userList = userService.getCurrentUserDAO().get().getRoom().getUserList();
//        messagingTemplate.convertAndSend(SockConst.SYS_WEB_USER_ROLES_INFO, userList);
//    }

    @RequestMapping(value = "/getIsSheriff", method = RequestMethod.GET)
    public Boolean checkSheriff(@RequestParam String login) throws IllegalGamePhaseException, NotEnoughRightsException, UserDoesNotExistInDBException, RoomsMismatchException {
        return gameService.isSheriff(login);
    }

    @RequestMapping(value = "/getIsMafia", method = RequestMethod.GET)
    public Boolean checkMafia(@RequestParam String login) throws IllegalGamePhaseException, NotEnoughRightsException, UserDoesNotExistInDBException, RoomsMismatchException {
        return gameService.isMafia(login);
    }
}
