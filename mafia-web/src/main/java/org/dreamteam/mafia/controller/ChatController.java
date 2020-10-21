package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.constants.SockConst;
import org.dreamteam.mafia.model.*;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.temporary.TemporaryDB;
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
import java.util.Map;

/**
 * Контроллер для чата
 */
@Controller
public class ChatController {

    @Autowired
    UserService userService;

    @Qualifier("Task")
    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @GetMapping(SockConst.REQUEST_GET_ROOMS)
    public @ResponseBody
    ArrayList<Room> getRooms() {
        if (!TemporaryDB.rooms.isEmpty()) {
            System.out.println(TemporaryDB.rooms.values());
            return new ArrayList<>(TemporaryDB.rooms.values());
        } else {
            return null;
        }
    }

    @PostMapping(SockConst.REQUEST_POST_CHECK_USER)
    public @ResponseBody
    Boolean checkUser(@RequestBody SystemMessage systemMessage) {
        String login = systemMessage.getUser().getLogin();

        //Добавляем нового пользователя в TemporaryDB, если его еще не существует.
        if (!TemporaryDB.users.containsKey("web:" + login)) {
            User user = new User();
            user.setId("web:" + login);
            user.setName(login);
            user.setLogin(login);
            TemporaryDB.users.put(user.getId(), user);

            System.out.println("MY: Пользователь " + systemMessage.getUser().getName() + " добавлен в TemporaryDB!");
            return true;
        } else {
            System.out.println("MY: Пользователь " + login + " существет в TemporaryDB!");
            return false;
        }
    }

    @PostMapping(SockConst.REQUEST_POST_CHECK_ROOM)
    public @ResponseBody
    Boolean checkRoom(@RequestBody Room room) {
        if (TemporaryDB.rooms.containsKey(room.getName())) {
            System.out.println("MY: Комната " + room.getName() + " уже существует.");
            return false;
        } else {
            TemporaryDB.rooms.put(room.getId(), room);
            System.out.println("MY: Комната " + room.getName() + " добавлена в Temporary DB!");
            return true;
        }
    }

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
        String login = userService.getCurrentUser().get().getLogin();

        // Отправляет информацию о добавленных комнатах всем пользователям.
        if (systemMessage.isNewRoom()) {
            messagingTemplate.convertAndSend(SockConst.SYS_WEB_ROOMS_CHAT, systemMessage);
        }
        System.out.println("");

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
        System.out.println("MY: От пользователя " + message.getFrom() + ", комната: " + message.getRoomName() + ".");
        String roomName = message.getRoomName();
        List<Message> messages;

        if (TemporaryDB.messagesByRooms.containsKey(message.getRoomName())) {
            // Добавляем новое сообщение в лист.
            messages = TemporaryDB.messagesByRooms.get(roomName);
        } else {
            messages = new ArrayList<>();
        }

        messages.add(message);
        TemporaryDB.messagesByRooms.put(roomName, messages);

        messagingTemplate.convertAndSend(SockConst.ROOM_WEB_CHAT + message.getRoomName(), message);
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
            Map<String, User> users = TemporaryDB.usersByRooms.get(room);
            System.out.println(users);
            for (Map.Entry<String, User> pair : users.entrySet()) {
                System.out.println("before: " + pair.getValue().getName() + " room: " + pair.getValue().getRoom());
                users.get(pair.getKey()).setRoom(null);
                System.out.println("after: " + pair.getValue().getName() + " room: " + pair.getValue().getRoom());
            }
        }
    }
}
