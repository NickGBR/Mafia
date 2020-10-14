package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dto.Response;
import org.dreamteam.mafia.dto.UserDTO;
import org.dreamteam.mafia.exceptions.UserRegistrationException;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    UserService userService;
    PasswordEncoder encoder;
    UserRepository repository;

    @Autowired
    public RegistrationController(UserService userService, PasswordEncoder encoder, UserRepository repository) {
        this.userService = userService;
        this.encoder = encoder;
        this.repository = repository;
    }

    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public Response registerAdmin() throws Exception {
        UserDAO dao = new UserDAO();
        dao.setLogin("admin");
        dao.setPassword(encoder.encode("admin"));
        repository.saveUser(dao);
            return new Response(ResultCode.SUCCESS, "Registration is successful");
    }

    @RequestMapping(method = RequestMethod.POST)
    public Response register(@RequestBody UserDTO dto) {
        System.out.println(dto);
        try {
            userService.registerNewUser(dto);
            return new Response(ResultCode.SUCCESS, "Registration is successful");
        } catch (UserRegistrationException e) {
            return new Response(e.getCode(), e.getLocalizedMessage());
        }
    }
}
