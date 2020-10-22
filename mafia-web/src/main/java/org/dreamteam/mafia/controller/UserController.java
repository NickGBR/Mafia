package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.dto.LoginDTO;
import org.dreamteam.mafia.dto.RegistrationDTO;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.security.SignedJsonWebToken;
import org.dreamteam.mafia.service.api.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Контроллер для регистрации и входа в систему пользователей
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Обрабатывает запросы на регистрацию
     *
     * @param dto - запрос на регистрацию
     * @return - JWT вновь зарегистрированного пользователя
     * @throws ClientErrorException - при ошибках регистрации
     *                              или при ошибках авторизации вновь зарегестрированного пользователя
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(
            @RequestBody RegistrationDTO dto) throws ClientErrorException {
        logger.debug("Incoming registration request. DTO: " + dto);
        userService.registerNewUser(dto);
        SignedJsonWebToken jws = userService.loginUser(dto.getLoginData());
        return jws.getToken();
    }

    /**
     * Обрабатывает запросы на вход в систему
     *
     * @param dto - запрос на вход в систему
     * @return - JWT пользователя
     * @throws ClientErrorException- при ошибках авторизации
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody LoginDTO dto) throws ClientErrorException {
        logger.debug("Incoming login request. DTO: " + dto);
        SignedJsonWebToken jws = userService.loginUser(dto);
        return jws.getToken();
    }

    /**
     * Обрабатывает запрос на получение имени текущего пользователя
     *
     * @return - имя текущего пользователя
     */
    @RequestMapping(value = "/getCurrentName", method = RequestMethod.GET)
    public String getCurrentUserName() {
        logger.debug("Incoming request for current username.");
        Optional<User> currentUser = userService.getCurrentUser();
        if (currentUser.isPresent()) {
            return currentUser.get().getLogin();
        } else {
            return "";
        }
    }
}
