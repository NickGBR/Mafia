package org.dreamteam.mafia.controller;


import org.dreamteam.mafia.bot.TBot;
import org.dreamteam.mafia.model.Room;
import org.dreamteam.mafia.model.Host;
import org.dreamteam.mafia.model.Message;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.temporary.TemporaryDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * Контроллер для чата
 */
@Controller
public class ChatController {

    @Autowired
    TBot bot;

    @Autowired
    UserService userService;

    @Qualifier("Task")
    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/civ_message")
    //@SendTo("/chat/civ_messages") //Можем использовать как комнату по умолчанию
    public void getCiviliansMessages(Message message) {
        message.setRole(Message.Role.CIVILIAN);
        messagingTemplate.convertAndSend("/chat/civ_messages/" + message.getRoom(), message);
    }

    @MessageMapping("/mafia_message")
    //@SendTo("/chat/mafia_messages/")  //Можем использовать как комнату по умолчанию
    public void getMafiaMessages(Message message) throws TelegramApiException {
        message.setRole(Message.Role.MAFIA);
        messagingTemplate.convertAndSend("/chat/mafia_messages/" + message.getRoom(), message);
    }

    /**
     * Данные метод принимает Json объект отправленный на "/system_message".
     *
     * @param room полученный Json преобразуется в объект Room.
     */
    @MessageMapping("/system_message")
    //@SendTo("/chat/mafia_messages/")  //Можем использовать как комнату по умолчанию
    public void getSystemMessages(Room room) {

        //Добавляем сообщение для вывода.
        if (room.getMessage() != null) {
            TemporaryDB.systemMessages.put(room.getName(), room.getMessage());
        }

        // Проверяем наличие команты в игре.
        if (!TemporaryDB.rooms.contains(room.getName())) {

            // Если комната новая, то добавляем ее в список существующих комнат.
            messagingTemplate.convertAndSend("/chat/system_messages/rooms/", room);
            TemporaryDB.rooms.add(room.getName());

            System.out.println("Комната " + room.getName() + " добавлена!");

            Host host = new Host(room.getName(), messagingTemplate, TemporaryDB.systemMessages);

            // Создаем нового ведущего для игры, и добавляем его в список храниящий всех ведущих работающих на сервере.
            ScheduledFuture<?> future = taskScheduler.scheduleWithFixedDelay(host, 10000);
            TemporaryDB.tasks.put(room.getName(), future);
        }

        // Проверяем была ли игра остановлена.
        if (room.isInterrupted()) {
            stopGame(room);
        }
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
        message.setMessage("Игра была остановлена!");
        message.setRoom(room.getName());
        message.setRole(Message.Role.HOST);
        message.setFrom("Host");

        // Отправляем сообщение в чат об остановке игры.
        messagingTemplate.convertAndSend("/chat/civ_messages/" + room.getName(), message);
    }

    /**
     * Удаляет пользователей из указанной комнаты при остановке игры.
     * Пользователи хранятся в "TemporaryDB.usersByRooms"
     * @param room пользователи данной комнаты будут удалены из нее.
     */
    private void cleanTelegramUsersRoom(String room) {
        if(!TemporaryDB.usersByRooms.isEmpty()){
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
