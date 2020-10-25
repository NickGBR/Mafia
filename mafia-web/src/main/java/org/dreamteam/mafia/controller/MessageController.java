package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.constants.SockConst;
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

    @PostMapping(SockConst.REQUEST_POST_MAFIA_MESSAGE)
    public void sendMessageToMafia(@RequestBody String message) throws ClientErrorException {
        messageService.sendMessage(message, SockConst.MAFIA_WEB_CHAT);
    }

    @PostMapping(SockConst.REQUEST_POST_CIVILIAN_MESSAGE)
    public void sendMessageToCivilians(@RequestBody String message) throws ClientErrorException {
        System.out.println(message);
        messageService.sendMessage(message, SockConst.CIV_WEB_CHAT);
    }

    @GetMapping("/restore")
    public List<OutgoingChatMessageDTO> sendMessage() throws ClientErrorException {
        return messageService.getChatHistory();
    }

}
