package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.exceptions.ClientErrorException;

/**
 * Интерфейс сервиса, обеспечивающего ведение самой игры.
 */
public interface GameService {

    /**
     * Меняет состояние игры на начатую,
     * проверят существует ли пользоватеь в базе данных,
     * проверяет достаточно ли у пльзователя прав для запуска комнаты,
     * отправляет всем пользователям информацию об успешном старте игры.
     *
     * @throws ClientErrorException - при возникновении ошибок на стороне клиента
     */
    void startGame() throws ClientErrorException;

    /**
     * Проверка является ли игрок шерифом.
     *
     * @param login - логин игрока
     * @return - true, если игрок явлеятся шерифом, нет в противном случае
     * @throws ClientErrorException - при возникновении ошибок на стороне клиента
     */
    boolean isSheriff(String login) throws ClientErrorException;

    /**
     * Проверка является ли игрок мафией/доном.
     *
     * @param login - логин игрока
     * @return - true, если игрок явлеятся мафией/доном, нет в противном случае
     * @throws ClientErrorException - при возникновении ошибок на стороне клиента
     */
    boolean isMafia(String login) throws ClientErrorException;

    /**
     * Подсчет голосов против игрока.
     *
     * @param login - логин игрока, против которого голосуют
     * @throws ClientErrorException - при возникновении ошибок на стороне клиента
     */
    void countVotesAgainst(String login) throws ClientErrorException;

    /**
     * Распределение ролей игрокам.
     *
     * @param room - игровая комната
     */
    void setRolesToUsers(RoomDAO room);
}