package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.dto.LoginDTO;
import org.dreamteam.mafia.dto.RegistrationDTO;
import org.dreamteam.mafia.exceptions.UserAuthenticationException;
import org.dreamteam.mafia.exceptions.UserRegistrationException;
import org.dreamteam.mafia.model.SignedJsonWebToken;
import org.dreamteam.mafia.service.api.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(
            @RequestBody RegistrationDTO dto) throws UserRegistrationException, UserAuthenticationException {
        logger.debug("Incoming registration request. DTO: " + dto);
        userService.registerNewUser(dto);
        SignedJsonWebToken jws = userService.loginUser(dto.getLoginData());
        return jws.getValue();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody LoginDTO dto) throws UserAuthenticationException {
        logger.debug("Incoming login request. DTO: " + dto);
        SignedJsonWebToken jws = userService.loginUser(dto);
        return jws.getValue();
    }
}
