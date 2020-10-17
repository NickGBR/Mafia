package org.dreamteam.mafia.controller;


import org.dreamteam.mafia.bot.TBot;
import org.dreamteam.mafia.model.Game;
import org.dreamteam.mafia.model.Host;
import org.dreamteam.mafia.model.Message;
import org.dreamteam.mafia.model.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
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

    //Хранит специальные сообщение от хоста.
    Map<String, Message> hostMessages = new HashMap<>();

    //Хранит текущие комнаты в игре.
    ArrayList<String> rooms = new ArrayList<>();

    //Хранит выполняемые задачи.
    Map<String, ScheduledFuture<?>> tasks = new HashMap<>();

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
        bot.sendMessage(message.getMessage());
    }

    /**
     * Данные метод принимает Json объект отправленный на "/host_message".
     *
     * @param game полученный Json преобразуется в объект Game.
     */
    @MessageMapping("/host_message")
    //@SendTo("/chat/mafia_messages/")  //Можем использовать как комнату по умолчанию
    public void getHostMessages(Game game) {

        //Добавляем сообщение для вывода.
        if (game.getMessage() != null) {
            hostMessages.put(game.getRoom(), game.getMessage());
        }

        // Проверяем наличие команты в игре.
        if (!rooms.contains(game.getRoom())) {

            // Если комната новая, то добавляем ее в список существующих комнат.
            rooms.add(game.getRoom());
            Host host = new Host(game.getRoom(), messagingTemplate, hostMessages);

            // Создаем нового ведущего для игры, и добавляем его в список храниящий всех ведущих работающих на сервере.
            ScheduledFuture<?> future = taskScheduler.scheduleWithFixedDelay(host, 10000);
            tasks.put(game.getRoom(), future);
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
        tasks.get(game.getRoom()).cancel(true);
        tasks.remove(game.getRoom());
        rooms.remove(game.getRoom());

        //Собираем сообщение для отправки в пользовательский чат
        Message message = new Message();
        message.setMessage("Игра была остановлена!");
        message.setRoom(game.getRoom());
        message.setRole(Message.Role.HOST);
        message.setFrom("Host");

        // Отправляем сообщение в чат об остановке игры.
        messagingTemplate.convertAndSend("/chat/civ_messages/" + game.getRoom(), message);
    }
}
