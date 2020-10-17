package org.dreamteam.mafia.temporary;

import org.dreamteam.mafia.model.Message;
import org.dreamteam.mafia.model.TelegramUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class TemporaryDB {

    /**Хранит текущие комнаты в игре*/
    public static List<String> rooms = new ArrayList<>();

    /** Хранит специальные сообщение от хоста.*/
    public static Map<String, Message> systemMessages = new HashMap<>();

    /** Хранит выполняемые задачи.*/
    public static Map<String, ScheduledFuture<?>> tasks = new HashMap<>();

    /** Хранит пользователей подключившихся через Телеграм.*/
    public static Map<String, TelegramUser> telegramUsers = new HashMap<>();

    /** Хранит пользователей подключившихся через Телеграм. Ключем является комната которая содержит в себе Map с
     * пользователями комнаты, досткуп к которым можно получить по id.*/
    public static Map<String, Map<String,TelegramUser>> telegramUsersByRooms = new HashMap<>();

}
