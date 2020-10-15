package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.dto.AuthenticationResponse;
import org.dreamteam.mafia.dto.LoginDTO;
import org.dreamteam.mafia.dto.RegistrationDTO;
import org.dreamteam.mafia.exceptions.UserAuthenticationException;
import org.dreamteam.mafia.exceptions.UserRegistrationException;
import org.dreamteam.mafia.model.SignedJsonWebToken;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public AuthenticationResponse register(@RequestBody RegistrationDTO dto) {
        System.out.println(dto);
        try {
            userService.registerNewUser(dto);
            SignedJsonWebToken jws = userService.loginUser(dto.getLoginData());
            return new AuthenticationResponse(ResultCode.SUCCESS.getValue(), "Registration is successful",
                                              jws.getValue());
        } catch (UserRegistrationException | UserAuthenticationException e) {
            return new AuthenticationResponse(e.getCode().getValue(), e.getLocalizedMessage(), "");
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public AuthenticationResponse login(@RequestBody LoginDTO dto) {
        System.out.println(dto);
        try {
            SignedJsonWebToken jws = userService.loginUser(dto);
            return new AuthenticationResponse(ResultCode.SUCCESS.getValue(), "Login is successful", jws.getValue());
        } catch (UserAuthenticationException e) {
            return new AuthenticationResponse(e.getCode().getValue(), e.getLocalizedMessage(), "");
        }
    }
}
