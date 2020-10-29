package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.constants.SockConst;
import org.dreamteam.mafia.dto.CharacterDisplayDTO;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.model.CharacterEnum;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.service.api.GameService;
import org.dreamteam.mafia.service.api.MessageService;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.util.ClientErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;
    private final UserService userService;
    private final MessageService messageService;
    private final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    public GameController(
            GameService gameService,
            UserService userService, MessageService messageService) {
        this.gameService = gameService;
        this.userService = userService;
        this.messageService = messageService;
    }

    @RequestMapping(value = SockConst.REQUEST_VOTE_FOR_USER, method = RequestMethod.GET)
    public void voteForUser(@RequestParam String login) throws ClientErrorException {
        gameService.countVotesAgainst(login);
    }

    @RequestMapping(value = SockConst.REQUEST_GET_OTHER_ROLE_INFO, method = RequestMethod.GET)
    public Boolean getRoleInfo(@RequestParam String login) throws ClientErrorException {
        final Optional<User> currentUser = userService.getCurrentUser();
        if (currentUser.orElseThrow(() -> new SecurityException("Unauthorised user can't check others roles"))
                .getCharacter().equals(CharacterEnum.DON)) {
            return gameService.isSheriff(login);
        } else if (currentUser.orElseThrow(() -> new SecurityException("Unauthorised user can't check others roles"))
                .getCharacter().equals(CharacterEnum.SHERIFF)) {
            return gameService.isMafia(login);
        }
        throw new ClientErrorException(ClientErrorCode.NOT_ENOUGH_RIGHTS,
                                       "Only sheriff or don players can perform this action");
    }

    @RequestMapping(value = SockConst.REQUEST_GET_START_GAME_INFO, method = RequestMethod.GET)
    public void startGame() throws ClientErrorException {
        gameService.startGame();
        messageService.sendGameStartUpdate();
    }

    @GetMapping(value = "/getRole")
    public CharacterEnum getRole() throws ClientErrorException {
        logger.debug("Incoming request for the self-role.");
        final CharacterEnum role = gameService.getRole();
        logger.debug("Role: " + role);
        return role;
    }

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

    /**
     * Обрабатывает запрос на получение списка пользователей в текущей игре
     *
     * @return - список пользователей в текущей игре в формате: имя, состояние, и статус мафии,
     * если запросивший сам мафия
     * @throws ClientErrorException - если текущий пользователь не находится в комнате
     *                              или игра не запущена
     */
    @GetMapping("/getCharacters")
    public List<CharacterDisplayDTO> getUsersInRoom() throws ClientErrorException {
        logger.debug("Incoming request for characters in the game");
        return gameService.getCharacterInGame();
    }
}
