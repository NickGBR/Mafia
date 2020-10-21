package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dto.CharacterDTO;
import org.dreamteam.mafia.exceptions.GameIsOverException;
import org.dreamteam.mafia.exceptions.GameNotStartedException;
import org.dreamteam.mafia.exceptions.IllegalGamePhaseException;
import org.dreamteam.mafia.exceptions.IllegalMoveException;
import org.dreamteam.mafia.model.Character;
import org.dreamteam.mafia.model.*;

import java.util.List;

/**
 * Интерфейс сервиса, обеспечивающего ведение самой игры.
 */
public interface GameService {

    /**
     * Находит игру, идущую в указанной комнате
     *
     * @param room - комната
     * @return - игра, идущая в комнате
     * @throws GameNotStartedException - если игра в комнате еще не началась
     */
    Room getGameInRoom(Room room) throws GameNotStartedException;

    /**
     * Возвращает список всех персонажей в игре
     *
     * @param room - игра
     * @return - список персонажей
     */
    List<Character> getCharactersInGame(Room room);

    /**
     * Возвращает список всех сообщений в чате игры
     *
     * @param room - игра
     * @return - список сообщений
     */
    List<Message> getMessageLog(Room room);

    /**
     * Переводит игру в следующую фазу
     *
     * @param room - игра
     * @throws GameIsOverException - если игра уже окончена
     */
    void advancePhase(Room room) throws GameIsOverException;

    /**
     * Выдвигает персонажа на голосование
     *
     * @param user         - выдвигающий игрок
     * @param characterDTO - выдвигаемый персонаж
     * @throws IllegalMoveException - если выдвижение нарушает правила игры
     */
    void nominateCharacter(User user, CharacterDTO characterDTO) throws IllegalMoveException;

    /**
     * Голосует против персонажа
     *
     * @param user         - голосующий игрок
     * @param characterDTO - голосуемый против персонаж
     * @throws IllegalMoveException - если голосование нарушает правила игры
     */
    void voteCharacter(User user, CharacterDTO characterDTO) throws IllegalMoveException;

    /**
     * Проверка является ли игрок шерифом.
     * @param login - логин игрока
     * @return - true, если игрок явлеятся шерифом, нет в противном случае
     * @throws IllegalGamePhaseException - если не соответствует фаза голосования
     *                      (днем голосуем против мафии/дона, ночью против шерифа)
     */
    boolean isSheriff(String login) throws IllegalGamePhaseException;

    /**
     * Проверка является ли игрок мафией/доном.
     * @param login - логин игрока
     * @return - true, если игрок явлеятся мафией/доном, нет в противном случае
     * @throws IllegalGamePhaseException - если не соответствует фаза голосования
     *                      (днем голосуем против мафии/дона, ночью против шерифа)
     */
    boolean isMafia(String login) throws IllegalGamePhaseException;
}
