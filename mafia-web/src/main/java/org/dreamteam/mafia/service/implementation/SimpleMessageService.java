package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.constants.SockConst;
import org.dreamteam.mafia.dto.CharacterUpdateDTO;
import org.dreamteam.mafia.dto.ChatMessageDTO;
import org.dreamteam.mafia.dto.GameDTO;
import org.dreamteam.mafia.dto.RoomDisplayDTO;
import org.dreamteam.mafia.entities.MessageEntity;
import org.dreamteam.mafia.entities.RoomEntity;
import org.dreamteam.mafia.entities.UserEntity;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.model.CharacterStatusEnum;
import org.dreamteam.mafia.model.DestinationEnum;
import org.dreamteam.mafia.model.MessageDestinationDescriptor;
import org.dreamteam.mafia.model.MessageRestorationDescriptor;
import org.dreamteam.mafia.repository.api.MessageRepository;
import org.dreamteam.mafia.service.api.MessageService;
import org.dreamteam.mafia.service.api.RoomService;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.util.ClientErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
        final Optional<UserEntity> from = userService.getCurrentUserDAO();
        if (!from.isPresent()) {
            throw new SecurityException("Non authorised user is not allowed to send messages");
        }
        final MessageDestinationDescriptor destinationDescriptor = roomService.getCurrentDestination();
        MessageEntity dao = new MessageEntity();
        dao.setText(message);
        dao.setUser(from.get());
        if (destinationDescriptor.getRoom().isPresent()) {
            dao.setRoom(destinationDescriptor.getRoom().get());
        }
        if (!destinationDescriptor.getDestination().equals(DestinationEnum.COMMON)
                && !destinationDescriptor.getDestination().equals(DestinationEnum.ROOM_USER)) {
            if (!from.get().getCharacterStatus().equals(CharacterStatusEnum.ALIVE)) {
                throw new ClientErrorException(ClientErrorCode.CHARACTER_IS_DEAD, "Dead characters can't use chat");
            }
        }
        dao.setDestination(destinationDescriptor.getDestination());
        dao = repository.save(dao);
        sendByDestination(destinationDescriptor, dao);
    }

    @Override
    public void sendSystemMessage(GameDTO systemMessage, MessageDestinationDescriptor descriptor) {
        MessageEntity dao = new MessageEntity();
        dao.setText(systemMessage.getMessage());
        if (descriptor.getRoom().isPresent()) {
            dao.setRoom(descriptor.getRoom().get());
        }
        dao.setDestination(descriptor.getDestination());
        repository.save(dao);
        if (descriptor.getRoom().isPresent()) {
            Long roomID = descriptor.getRoom().get().getRoomId();
            messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT + roomID,
                                             systemMessage);
        } else {
            messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHAT,
                                             systemMessage);
        }
    }

    @Override
    public void sendVotingResultUpdate(
            CharacterUpdateDTO dto, RoomEntity room) {
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_CHARACTER_INFO_UPDATE + room.getRoomId(),
                                         dto);
    }

    @Override
    public List<ChatMessageDTO> getChatHistory() {
        final Optional<UserEntity> from = userService.getCurrentUserDAO();
        if (!from.isPresent()) {
            throw new SecurityException("Non authorised user is not allowed to send messages");
        }
        List<ChatMessageDTO> dtos = new ArrayList<>();
        final MessageRestorationDescriptor descriptor = roomService.getPermittedToRestorationDestinations();
        // Ограничиваем выборку сообщений из базы 500 записями
        Pageable pageable = PageRequest.of(0, 500, Sort.Direction.DESC, "messageId");
        if (descriptor.getRoom().isPresent()) {
            repository.findByRoomAndDestinationIn(descriptor.getRoom().get(), descriptor.getDestinations(), pageable)
                    .stream()
                    .map(ChatMessageDTO::new)
                    .collect(Collectors.toCollection(() -> dtos));
        } else {
            repository.findByDestinationIn(descriptor.getDestinations(), pageable)
                    .stream()
                    .map(ChatMessageDTO::new)
                    .collect(Collectors.toCollection(() -> dtos));
        }
        Collections.reverse(dtos);
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
    public void sendJoinUpdate(RoomDisplayDTO room, String login) {
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_USERS_INFO + room.getId(),
                                         login);
    }

    @Override
    public void sendKickUpdate(RoomDisplayDTO room, String login) {
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_USERS_INFO_KICKED + room.getId(),
                                         login);
    }

    @Override
    public void sendReadinessUpdate() throws ClientErrorException {
        messagingTemplate.convertAndSend(SockConst.SYS_USERS_READY_TO_PLAY_INFO
                                                 + roomService.getCurrentRoom().getId(),
                                         roomService.isRoomReady());
    }

    @Override
    public void sendGameStartUpdate() throws ClientErrorException {
        messagingTemplate.convertAndSend(SockConst.SYS_GAME_STARTED_INFO
                                                 + roomService.getCurrentRoom().getId(), true);
    }

    /**
     * Отправляет сообщения подписчикам, в зависимости от роли.
     *
     * @param destinationDescriptor - описание цели сообщения: адреса и, возможно, его комната
     * @param message               - сообщение для передачи.
     */
    private void sendByDestination(MessageDestinationDescriptor destinationDescriptor, MessageEntity message) {
        Long roomID = 0L;
        if (destinationDescriptor.getDestination() != DestinationEnum.COMMON) {
            roomID = destinationDescriptor.getRoom().orElseThrow(
                    () -> new RuntimeException("Logic error: server generated destination descriptor is invalid.")
            ).getRoomId();
        }
        switch (destinationDescriptor.getDestination()) {
            case MAFIA:
                messagingTemplate.convertAndSend(SockConst.MAFIA_WEB_CHAT + roomID,
                                                 new ChatMessageDTO(message));
                break;
            case CIVILIAN:
                messagingTemplate.convertAndSend(SockConst.CIV_WEB_CHAT + roomID,
                                                 new ChatMessageDTO(message));
                break;
            case ROOM_USER:
                messagingTemplate.convertAndSend(SockConst.ROOM_WEB_CHAT + roomID,
                                                 new ChatMessageDTO(message));
                break;
            case COMMON: {
                messagingTemplate.convertAndSend(SockConst.ROOM_WEB_CHAT,
                                                 new ChatMessageDTO(message));
            }
        }
    }
}
