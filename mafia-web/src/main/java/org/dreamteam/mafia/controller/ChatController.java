package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.bot.TBot;
import org.dreamteam.mafia.constants.WebChatConst;
import org.dreamteam.mafia.model.*;
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
        messagingTemplate.convertAndSend(WebChatConst.CIV_WEB_CHAT + message.getRoom(), message);
    }

    @MessageMapping("/mafia_message")
    //@SendTo("/chat/mafia_messages/")  //Можем использовать как комнату по умолчанию
    public void getMafiaMessages(Message message) throws TelegramApiException {
        message.setRole(Message.Role.MAFIA);
        messagingTemplate.convertAndSend(WebChatConst.MAFIA_WEB_CHAT + message.getRoom(), message);
    }

    /**
     * Данные метод принимает Json объект отправленный на "/system_message".
     *
     * @param systemMessage полученный Json преобразуется в объект SystemMessage.
     */
    @MessageMapping("/system_message")
    //@SendTo("/chat/mafia_messages/")  //Можем использовать как комнату по умолчанию
    public void getSystemMessages(SystemMessage systemMessage) {

        //Добавляем нового пользователя в базу, его еще не существует.
        if (systemMessage.isNewUser()) {

                String login = userService.getCurrentUser().get().getLogin();
                User user = new User();
                user.setId("web:" + login);
                user.setName(login);
                user.setLogin(login);
                TemporaryDB.users.put(user.getId(), user);

                System.out.println("MY: Пользователь " + user.getName() + " добвлен в TemporaryDB!");

                messagingTemplate.convertAndSendToUser(userService.getCurrentUser().get().getLogin(),
                        WebChatConst.SYS_WEB_CHAT, systemMessage);
        }

        //Добавляем сообщение для вывода.
        if (systemMessage.getMessage() != null) {
            TemporaryDB.systemMessages.put(systemMessage.getRoom().getName(), systemMessage.getMessage());
        }

        // Проверяем наличие команты в игре.
        if (systemMessage.getRoom() != null) {
            if (!TemporaryDB.rooms.contains(systemMessage.getRoom().getName())) {

                // Если комната новая, то добавляем ее в список существующих комнат.
                messagingTemplate.convertAndSend(WebChatConst.SYS_WEB_CHAT_ROOMS, systemMessage);
                TemporaryDB.rooms.add(systemMessage.getRoom().getName());

                System.out.println("MY: Комната " + systemMessage.getRoom().getName() + " добавлена!");

                Host host = new Host(systemMessage.getRoom().getName(), messagingTemplate, TemporaryDB.systemMessages);

                // Создаем нового ведущего для игры, и добавляем его в список храниящий всех ведущих работающих на сервере.
                ScheduledFuture<?> future = taskScheduler.scheduleWithFixedDelay(host, 10000);
                TemporaryDB.tasks.put(systemMessage.getRoom().getName(), future);
            }
        }

        // Проверяем была ли игра остановлена.
        if (systemMessage.getRoom() != null) {
            if (systemMessage.getRoom().isInterrupted()) {
                stopGame(systemMessage.getRoom());
            }
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
        message.setText("Игра была остановлена!");
        message.setRoom(room.getName());
        message.setRole(Message.Role.HOST);
        message.setFrom("Host");

        // Отправляем сообщение в чат об остановке игры.
        messagingTemplate.convertAndSend(WebChatConst.CIV_WEB_CHAT + room.getName(), message);
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
