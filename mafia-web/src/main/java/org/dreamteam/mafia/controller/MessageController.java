package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.dto.OutgoingChatMessageDTO;
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

    @PostMapping("/send")
    public void sendMessage(@RequestBody String message) throws ClientErrorException {
        messageService.sendMessage(message);
    }

    @GetMapping("/restore")
    public List<OutgoingChatMessageDTO> sendMessage() throws ClientErrorException {
        return messageService.getChatHistory();
    }
}
