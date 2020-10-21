package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.exceptions.IllegalGamePhaseException;
import org.dreamteam.mafia.exceptions.NotEnoughRightsException;
import org.dreamteam.mafia.exceptions.RoomsMismatchException;
import org.dreamteam.mafia.exceptions.UserDoesNotExistInDBException;
import org.dreamteam.mafia.service.api.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/room")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(value = "/getIsSheriff", method = RequestMethod.GET)
    public Boolean checkSheriff(@RequestParam String login) throws IllegalGamePhaseException, NotEnoughRightsException, UserDoesNotExistInDBException, RoomsMismatchException {
        return gameService.isSheriff(login);
    }

    @RequestMapping(value = "/getIsMafia", method = RequestMethod.GET)
    public Boolean checkMafia(@RequestParam String login) throws IllegalGamePhaseException {
        return gameService.isMafia(login);
    }
}
