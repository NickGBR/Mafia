package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.constants.GameConst;
import org.dreamteam.mafia.constants.SockConst;
import org.dreamteam.mafia.model.*;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.temporary.TemporaryDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


/**
 * Контроллер для чата
 * userId  = web: + login;
 */
@Controller
//@RequestMapping
public class GameChatControllerJS {


    private final Logger logger = LoggerFactory.getLogger(GameChatControllerJS.class);

    @Autowired
    UserService userService;

    @Qualifier("Task")
    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping(SockConst.CIV_END_POINT)
    //@SendTo("/chat/civ_messages") //Можем использовать как комнату по умолчанию
    public void getCiviliansMessages(Message message) {
        message.setRole(Message.Role.CIVILIAN);
        messagingTemplate.convertAndSend(SockConst.CIV_WEB_CHAT + message.getRoomName(), message);
    }

    @MessageMapping(SockConst.MAFIA_END_POINT)
    //@SendTo("/chat/mafia_messages/")  //Можем использовать как комнату по умолчанию
    public void getMafiaMessages(Message message) throws TelegramApiException {
        message.setRole(Message.Role.MAFIA);
        messagingTemplate.convertAndSend(SockConst.MAFIA_WEB_CHAT + message.getRoomName(), message);
    }

    /**
     * Данные метод принимает Json объект отправленный на "/system_message".
     *
     * @param systemMessage полученный Json преобразуется в объект SystemMessage.
     */
    @MessageMapping(SockConst.SYSTEM_END_POINT)
    public void getSystemMessages(SystemMessage systemMessage) {
        logger.debug("Incoming system  message: " + systemMessage);

        // Добавляем сообщение для вывода.
        if (systemMessage.getMessage() != null) {
            TemporaryDB.systemMessages.put(systemMessage.getRoom().getName(), systemMessage.getMessage());
        }

        // Проверяем была ли игра остановлена.
        if (systemMessage.getRoom() != null) {
            if (systemMessage.getRoom().isInterrupted()) {
                stopGame(systemMessage.getRoom());
            }
        }
    }

    @MessageMapping(SockConst.ROOM_END_POINT)
    public void getRoomMessages(Message message) {
        System.out.println("MY: Получено сообщение " + message.getText() + ",");
        System.out.println("    От пользователя " + message.getFrom() + ", комната: " + message.getRoomName() + ".");
        String roomId = message.getRoomID();
        List<Message> messages;

        if (TemporaryDB.messagesByRooms.containsKey(message.getRoomName())) {
            // Добавляем новое сообщение в лист.
            messages = TemporaryDB.messagesByRooms.get(roomId);
        } else {
            messages = new ArrayList<>();
        }

        messages.add(message);
        TemporaryDB.messagesByRooms.put(roomId, messages);
        messagingTemplate.convertAndSend(SockConst.ROOM_WEB_CHAT + message.getRoomID(), message);
    }

    /**
     * Метод для остановки игры.
     *
     * @param room сессия игры которую необходимо остановить.
     */
    private void stopGame(Room room) {
        // Если игра остановлена то останавливаем текущую задачу, удаляем задачу из списка задач.
        TemporaryDB.tasks.get(room.getName()).cancel(true);
        TemporaryDB.tasks.remove(room.getName());
        TemporaryDB.rooms.remove(room.getName());
        cleanTelegramUsersRoom(room.getName());

        //Собираем сообщение для отправки в пользовательский чат
        Message message = new Message();
        message.setText("Игра была остановлена!");
        message.setRoomName(room.getName());
        message.setRole(Message.Role.HOST);
        message.setFrom("Host");

        // Отправляем сообщение в чат об остановке игры.
        messagingTemplate.convertAndSend(SockConst.CIV_WEB_CHAT + room.getName(), message);
    }

    /**
     * Удаляет пользователей из указанной комнаты при остановке игры.
     * Пользователи хранятся в "TemporaryDB.usersByRooms"
     *
     * @param room пользователи данной комнаты будут удалены из нее.
     */
    private void cleanTelegramUsersRoom(String room) {
        if (!TemporaryDB.usersByRooms.isEmpty()) {
            List<User> users = TemporaryDB.usersByRooms.get(room);
            System.out.println(users);
            for (User user : users) {
                System.out.println("before: " + user.getName() + " room: " + user.getRoom());
                user.setRoom(null);
                System.out.println("after: " + user.getName() + " room: " + user.getRoom());
            }
        }
    }

}
