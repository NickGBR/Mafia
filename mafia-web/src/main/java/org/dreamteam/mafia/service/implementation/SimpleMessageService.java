package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.constants.SockConst;
import org.dreamteam.mafia.dao.MessageDAO;
import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dto.OutgoingChatMessageDTO;
import org.dreamteam.mafia.dto.RoomDisplayDTO;
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

    @Override
    public void sendAddRoom(RoomDisplayDTO addedRoom) {
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_ROOMS_INFO_ADD, addedRoom);
    }

    @Override
    public void sendRemoveRoom(RoomDisplayDTO removedRoom) {
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_ROOMS_INFO_REMOVE, removedRoom);
        // Уведолмяем игроков в комнате о том, что комната была распущена.
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_ROOMS_INFO_REMOVE + removedRoom.getId(),
                                         "");
    }

    @Override
    public void sendUpdateRoom(RoomDisplayDTO updatedRoom) {
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_ROOMS_INFO_UPDATE, updatedRoom);
    }

    @Override
    public void sendJoinUpdate(RoomDisplayDTO room) {
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_USERS_INFO + room.getId(),
                                         true);
    }

    @Override
    public void sendReadinessUpdate() throws ClientErrorException {
        messagingTemplate.convertAndSend(SockConst.SYS_USERS_READY_TO_PLAY_INFO
                                                 + roomService.getCurrentRoom().getId(),
                                         roomService.isRoomReady());
    }
}
