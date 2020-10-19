package org.dreamteam.mafia.controller;


import org.dreamteam.mafia.bot.TBot;
import org.dreamteam.mafia.model.Game;
import org.dreamteam.mafia.model.Host;
import org.dreamteam.mafia.model.Message;
import org.dreamteam.mafia.model.User;
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
     * @param game полученный Json преобразуется в объект Game.
     */
    @MessageMapping("/system_message")
    //@SendTo("/chat/mafia_messages/")  //Можем использовать как комнату по умолчанию
    public void getHostMessages(Game game) {

        //Добавляем сообщение для вывода. Хост отправляет сообщение на сервер, которве выводится в игре.
        if (game.getMessage() != null) {
            TemporaryDB.systemMessages.put(game.getRoom(), game.getMessage());
        }

        // Проверяем наличие команты в игре.
        if (!TemporaryDB.rooms.contains(game.getRoom())) {

            // Если комната новая, то добавляем ее в список существующих комнат.
            TemporaryDB.rooms.add(game.getRoom());
            Host host = new Host(game.getRoom(), messagingTemplate, TemporaryDB.systemMessages);

            // Создаем нового ведущего для игры, и добавляем его в список храниящий всех ведущих работающих на сервере.
            ScheduledFuture<?> future = taskScheduler.scheduleWithFixedDelay(host, 10000);
            TemporaryDB.tasks.put(game.getRoom(), future);
        }

        // Проверяем была ли игра остановлена.
        if (game.isInterrupted()) {
            stopGame(game);
        }
    }

    /**
     * Метод для остановки игры.
     *
     * @param game сессия игры которую необходимо остановить.
     */
    private void stopGame(Game game) {
        // Если игра остановлена то останавливаем текущую задачу, удаляем задачу из списка задач.
        TemporaryDB.tasks.get(game.getRoom()).cancel(true);
        TemporaryDB.tasks.remove(game.getRoom());
        TemporaryDB.rooms.remove(game.getRoom());
        cleanTelegramUsersRoom(game.getRoom());

        //Собираем сообщение для отправки в пользовательский чат
        Message message = new Message();
        message.setText("Игра была остановлена!");
        message.setRoom(game.getRoom());
        message.setRole(Message.Role.HOST);
        message.setFrom("Host");

        // Отправляем сообщение в чат об остановке игры.
        messagingTemplate.convertAndSend("/chat/civ_messages/" + game.getRoom(), message);
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
