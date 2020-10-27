package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.exceptions.*;

/**
 * Интерфейс сервиса, обеспечивающего ведение самой игры.
 */
public interface GameService {

    /**
     * Меняет состояние игры на начатую,
     * проверят существует ли пользоватеь в базе данных,
     * проверяет достаточно ли у пльзователя прав для запуска комнаты,
     * отправляет всем пользователям информацию об успешном старте игры.
     */
    void startGame() throws ClientErrorException;

    /**
     * Проверка является ли игрок шерифом.
     *
     * @param login - логин игрока
     * @return - true, если игрок явлеятся шерифом, нет в противном случае
     * @throws IllegalGamePhaseException     - если не соответствует фаза дня (ночью шериф ищет мафию/дона, мафия ищет шерифа)
     * @throws UserDoesNotExistInDBException - если игрока нет в базе
     * @throws RoomsMismatchException        - если игроки находятся в разных комнатах
     * @throws NotEnoughRightsException      - если у игрока нет прав голосовать в данную фазу
     */
    boolean isSheriff(String login) throws IllegalGamePhaseException, UserDoesNotExistInDBException,
            RoomsMismatchException, NotEnoughRightsException;

    /**
     * Проверка является ли игрок мафией/доном.
     *
     * @param login - логин игрока
     * @return - true, если игрок явлеятся мафией/доном, нет в противном случае
     * @throws IllegalGamePhaseException     - если не соответствует фаза дня (ночью шериф ищет мафию/дона, мафия ищет шерифа)
     * @throws UserDoesNotExistInDBException - если игрока нет в базе
     * @throws RoomsMismatchException        - если игроки находятся в разных комнатах
     * @throws NotEnoughRightsException      - если у игрока нет прав голосовать в данную фазу
     */
    boolean isMafia(String login) throws IllegalGamePhaseException, UserDoesNotExistInDBException,
            RoomsMismatchException, NotEnoughRightsException;

    /**
     * Подсчет голосов против игрока.
     *
     * @param login - логин игрока, против которого голосуют
     * @throws RoomsMismatchException        - если игроки находятся в разных комнатах
     * @throws UserDoesNotExistInDBException - если игрока нет в базе
     * @throws IllegalGamePhaseException     - если не соответствует фаза голосования (мирные голосуют днем)
     * @throws CharacterAlreadyDeadException - если пользователь уже выбыл из игры
     */
    void countVotesAgainst(String login) throws RoomsMismatchException, UserDoesNotExistInDBException,
            IllegalGamePhaseException, CharacterAlreadyDeadException;

    /**
     * Распределение ролей игрокам.
     *
     * @param room - игровая комната
     */
    void setRolesToUsers(RoomDAO room);
}