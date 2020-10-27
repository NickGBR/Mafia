package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.constants.SockConst;
import org.dreamteam.mafia.dao.enums.CharacterEnum;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.service.api.GameService;
import org.dreamteam.mafia.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room")
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    @Autowired
    public GameController(GameService gameService,
                          UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @RequestMapping(value = SockConst.REQUEST_GET_VOTE_FOR_USER, method = RequestMethod.GET)
    public @ResponseBody void voteForUser(@RequestParam String login) throws ClientErrorException {
        System.out.println(login);

       // gameService.voteCharacter(userService.getCurrentUser());
    }

    @RequestMapping(value = SockConst.REQUEST_GET_ROLE_INFO, method = RequestMethod.GET)
    public @ResponseBody
    Boolean startGame(@RequestParam String login) throws ClientErrorException {
        System.out.println(login);
        if (userService.getCurrentUserDAO().get().getCharacter().equals(CharacterEnum.DON)) {
            return gameService.isSheriff(login);
        } else if (userService.getCurrentUserDAO().get().getCharacter().equals(CharacterEnum.SHERIFF)) {
            return gameService.isMafia(login);
        }
        return null;
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

    @Deprecated
    @RequestMapping(value = "/getIsSheriff", method = RequestMethod.GET)
    public Boolean checkSheriff(@RequestParam String login) throws ClientErrorException {
        return gameService.isSheriff(login);
    }

    @Deprecated
    @RequestMapping(value = "/getIsMafia", method = RequestMethod.GET)
    public Boolean checkMafia(@RequestParam String login) throws ClientErrorException {
        return gameService.isMafia(login);
    }
}
