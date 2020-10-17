package org.dreamteam.mafia.temporary;

import org.dreamteam.mafia.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class TemporaryDB {

    //Хранит текущие комнаты в игре
    public static List<String> rooms = new ArrayList<>();

    //Хранит специальные сообщение от хоста.
    public static Map<String, Message> systemMessages = new HashMap<>();

    //Хранит выполняемые задачи.
    public static Map<String, ScheduledFuture<?>> tasks = new HashMap<>();
}
