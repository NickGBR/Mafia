package org.dreamteam.mafia.temporary;

import org.dreamteam.mafia.model.Message;
import org.dreamteam.mafia.model.Room;
import org.dreamteam.mafia.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class TemporaryDB {

    /**
     * Хранит текущие комнаты в игре, key = roomId.
     */
    public static Map<String, Room> rooms = new HashMap<>();

    /**
     * Хранит специальные сообщение от хоста.
     */
    public static Map<String, Message> systemMessages = new HashMap<>();

    /**
     * Хранит выполняемые задачи.
     */
    public static Map<String, ScheduledFuture<?>> tasks = new HashMap<>();

    /**
     * Хранит пользователей подключившихся через Web  в виде ключ web: + userName, значение User user,
     * пользователей подключвшихся черех Telegram в виде ключ @t: + userName, значение User user.
     */
    public static Map<String, User> users = new HashMap<>();

    /**
     * Хранит пользователей вошедших в конкретную комнату. Ключем является комната которая содержит в себе List с
     * пользователями комнаты, доступ к которым можно получить по id.
     */
    public static Map<String, List<User>> usersByRooms = new HashMap<>();


    /**
     * Хранит сообщения пользователей, в конкретных комнатах. Key = roomId
     */
    public static Map<String, List<Message>> messagesByRooms = new HashMap<>();

}
