package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.constants.SockConst;
import org.dreamteam.mafia.dao.MessageDAO;
import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dto.OutgoingChatMessageDTO;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.repository.api.MessageRepository;
import org.dreamteam.mafia.service.api.MessageService;
import org.dreamteam.mafia.service.api.RoomService;
import org.dreamteam.mafia.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimpleMessageService implements MessageService {

    private final UserService userService;
    private final RoomService roomService;
    private final MessageRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public SimpleMessageService(
            UserService userService, RoomService roomService,
            MessageRepository repository,
            SimpMessagingTemplate messagingTemplate) {
        this.userService = userService;
        this.roomService = roomService;
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendMessage(String message) throws ClientErrorException {
        final Optional<UserDAO> from = userService.getCurrentUserDAO();
        if (!from.isPresent()) {
            throw new SecurityException("Non authorised user is not allowed to send messages");
        }
        RoomDAO room = roomService.getCurrentRoomDAO();
        MessageDAO dao = new MessageDAO();
        dao.setText(message);
        dao.setUser(from.get());
        dao.setRoom(room);
        dao = repository.save(dao);
        messagingTemplate.convertAndSend(SockConst.ROOM_WEB_CHAT + room.getRoomId(),
                                         new OutgoingChatMessageDTO(dao));
    }

    @Override
    public List<OutgoingChatMessageDTO> getChatHistory() throws ClientErrorException {
        final Optional<UserDAO> from = userService.getCurrentUserDAO();
        if (!from.isPresent()) {
            throw new SecurityException("Non authorised user is not allowed to send messages");
        }
        List<OutgoingChatMessageDTO> dtos = new ArrayList<>();
        RoomDAO room = roomService.getCurrentRoomDAO();
        repository.findByRoom(room).stream()
                .map(OutgoingChatMessageDTO::new)
                .collect(Collectors.toCollection(() -> dtos));
        return dtos;
    }
}
