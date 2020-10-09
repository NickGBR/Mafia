package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.model.*;
import org.dreamteam.mafia.model.Character;

import java.util.List;

public interface GameService {

    /**
     * Возвращает роль персонажа пользователя в данной игре
     * @param game - игра
     * @param user - пользователь
     * @return - роль пользователя
     */
    Role getRole(Game game, User user);

    /**
     * Возвращает список всех персонажей в игре
     * @param game - игра
     * @return - список персонажей
     */
    List<Character> getCharactersInGame(Game game);

    /**
     * Возвращает список всех сообщений в чате игры
     * @param game - игра
     * @return - список сообщений
     */
    List<Message> getMessageLog(Game game);

    /**
     * Возвращает список всех живых персонажей в игре
     * @param game - игра
     * @return - список
     */
    List<Character> getAliveCharactersInGame(Game game);

    /**
     * Возвращает игрока, который будет выступать следующим в ходе дневных выступлений
     * @param game - игра
     * @return - пользователь, который будет выступать следующим
     */
    User getNextSpeaker(Game game);

}
