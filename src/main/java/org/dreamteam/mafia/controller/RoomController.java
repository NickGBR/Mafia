package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.dto.Response;
import org.dreamteam.mafia.util.ResultCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    @RequestMapping(value = "/getAvailable", method = RequestMethod.GET)
    public Response register() {
        return new Response(ResultCode.SUCCESS, "GET is successful");
    }
}
