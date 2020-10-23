package org.dreamteam.mafia.controller.temporary;
import org.dreamteam.mafia.constants.GameConst;
import org.dreamteam.mafia.constants.SockConst;
import org.dreamteam.mafia.model.Room;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class RoomListControllerJS {

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

    /**
     * Добавляет пользователя в комнату.
     */
    @GetMapping(SockConst.REQUEST_GET_ADD_USER_TO_ROOM)
    public @ResponseBody
    Integer addUserToRoom(@RequestParam String roomName) {

        String login = userService.getCurrentUser().get().getLogin();

        User user = TemporaryDB.users.get("web:" + login);

        // Получаем список пользователей находящихся в комнате.
        List<User> users = TemporaryDB.usersByRooms.get(roomName);

        if (users.size() == GameConst.USERS_AMOUNT) {
            //users.remove(user);
            System.out.println("MY: список пользователей комнаты, " + roomName + ": " + users);
            return GameConst.FULL_ROOM;
        }

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


        System.out.println("MY: список пользователей комнаты, " + roomName + ": " + users);
        return users.size();
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


}
