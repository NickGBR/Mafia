package org.dreamteam.mafia.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    @RequestMapping(value = "/getAvailable", method = RequestMethod.GET)
    public String register() {
        return "GET is successful";
    }
}
