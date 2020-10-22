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


/**
 * Контроллер для чата
 * userId  = web: + login;
 */
@Controller
//@RequestMapping
public class ChatController {

    @Autowired
    UserService userService;

    @Qualifier("Task")
    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    /**
     * Метод получения списка комнанат, для нового пользователя, вошедшего на страницу выбора комнат.
     *
     * @return
     */
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

            System.out.println("MY: Пользователь " + systemMessage.getUser().getName()
                    + " добавлен в TemporaryDB! userId = " + user.getId() + ".");
            return true;
        } else {
            System.out.println("MY: Пользователь " + login + " существет в TemporaryDB!");
            return false;
        }
    }

    /**
     * Проверяем наличие комнаты. Если комната существует, отклоняем запрос на создаение комнаты.
     * Также создаем для новой комнаты базу хранящую сообщения отправленные в этой комнате.
     *
     * @param room
     * @return
     */
    @PostMapping(SockConst.REQUEST_POST_CHECK_ROOM)
    public @ResponseBody
    Boolean checkRoom(@RequestBody Room room) {
        String login = userService.getCurrentUser().get().getLogin();
        String roomName = room.getName();

        // Проверяем существует ли указанная комната.
        if (TemporaryDB.rooms.containsKey(room.getName())) {
            if (TemporaryDB.rooms.containsKey(roomName))
                System.out.println("MY: Комната " + room.getName() + " уже существует.");
            return false;
        } else {


            /* Если комната новая, то делаем пользоателя администратором этой комнаты,
               и добавляем его в нее, как в БД так и на пользователькой стороне.*/

            //Добавляем новую комнату в список комнат.
            TemporaryDB.rooms.put(room.getId(), room);

            // Так как комната новая, то пользователь создавший ее, будет модератором.
            User user = TemporaryDB.users.get("web:" + login);

            // Создаем список пользователей находящихся в данной комнате.
            List<User> users = new ArrayList<>();

            // Добавляем в список пользовотелей нашего пользователя, он будет первым.
            user.setRoom(roomName);
            users.add(user);

            // Далаем нашего пользователя Админимтратором комнаты.
            TemporaryDB.rooms.get(roomName).setAdmin(user);

            //Добавляем пользователей текущей комнаты, в их комнату.
            TemporaryDB.usersByRooms.put(roomName, users);
            System.out.println(TemporaryDB.rooms.get(roomName).getAdmin());
            System.out.println("MY: Комната " + room.getName() + " добавлена в Temporary DB! Админстратор комнаты: " + user.getName());
            return true;
        }
    }

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
     * Добавляет пользователя в комнату.
     *
     * @param roomName
     * @return
     */
    @GetMapping(SockConst.REQUEST_GET_ADD_USER_TO_ROOM)
    public @ResponseBody
    List<User> addUserToRoom(@RequestParam String roomName) {
        String login = userService.getCurrentUser().get().getLogin();

        User user = TemporaryDB.users.get("web:" + login);

        // Получаем список пользователей находящихся в комнате.
        List<User> users = TemporaryDB.usersByRooms.get(roomName);
        System.out.println("MY: список пользователей комнаты, " + roomName + ": " + users);

        // Добавляем в список пользовотелей нашего пользователя.
        if (!users.contains(user)) {
            user.setRoom(roomName);
            users.add(user);
        }

        //Добавляем пользователей текущей комнаты, в их комнату.
        TemporaryDB.usersByRooms.put(roomName, users);
        System.out.println(users);
        System.out.println(TemporaryDB.rooms.get(roomName).getAdmin());
        User admin = TemporaryDB.rooms.get(roomName).getAdmin();
        messagingTemplate.convertAndSend(SockConst.SYS_WEB_USERS_INFO + roomName, users);

        if (admin != null) {
            System.out.println("MY: пользователь " + user.getName() + " добавлен в комнату " + roomName +
                    " администатор комнаты " + admin.getName());
        }

        return users;
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
            messagingTemplate.convertAndSend(SockConst.SYS_WEB_ROOMS_INFO, systemMessage);
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
        System.out.println("    От пользователя " + message.getFrom() + ", комната: " + message.getRoomName() + ".");
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
