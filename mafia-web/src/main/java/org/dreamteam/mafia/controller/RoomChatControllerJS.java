package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.constants.GameConst;
import org.dreamteam.mafia.constants.SockConst;
import org.dreamteam.mafia.model.Message;
import org.dreamteam.mafia.model.SystemMessage;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.temporary.TemporaryDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RoomChatControllerJS {

    @Autowired
    UserService userService;

    @Qualifier("Task")
    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    /**
     * Данные метод отпраляем пользователю сообщение оставленные в комнате, до его захода.
     */
    @GetMapping(SockConst.REQUEST_GET_MESSAGES)
    public @ResponseBody
    List<Message> getMessages(@RequestParam String roomName) {
        if (TemporaryDB.messagesByRooms.get(roomName).isEmpty()) {
            return null;
        } else return TemporaryDB.messagesByRooms.get(roomName);
    }

    @GetMapping(SockConst.REQUEST_GET_USERS)
    public @ResponseBody
    List<User> getUsers(@RequestParam String roomName) {
        return TemporaryDB.usersByRooms.get(roomName);
    }

    @GetMapping(SockConst.REQUEST_GET_ROOM_ADMIN_NAME)
    public @ResponseBody
    String getRoomAdminName(@RequestParam String roomName) {
        return TemporaryDB.rooms.get(roomName).getAdmin().getName();
    }

    /**
     * Меняем готовность пользователя, возращаем его готовность.
     */
    @GetMapping(SockConst.REQUEST_GET_CHANGE_READY_STATUS)
    public @ResponseBody
    Boolean changeUserReadyStatus(@RequestParam String userName) {
        User user = TemporaryDB.users.get("web:" + userName);
        user.setReady(!user.isReady());
        return user.isReady();
    }
}
