package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.constants.SockConst;
import org.dreamteam.mafia.dto.ChatMessageDTO;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.service.api.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(SockConst.REQUEST_SEND_MESSAGE)
    public void sendMessage(@RequestBody String message) throws ClientErrorException {
        messageService.sendMessage(message);
    }

    @GetMapping(SockConst.REQUEST_LOAD_MESSAGES)
    public List<ChatMessageDTO> sendMessage() throws ClientErrorException {
        return messageService.getChatHistory();
    }

}
