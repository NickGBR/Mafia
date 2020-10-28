package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.enums.GameEndStatus;
import org.dreamteam.mafia.dto.CharacterDisplayDTO;
import org.dreamteam.mafia.exceptions.ClientErrorException;

import java.util.List;

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

    /**
     * Возвращает список персонажей в игре: их имена, состояния и роли, если запрашивающий имеет право их видеть
     *
     * @return - список персонажей
     */
    List<CharacterDisplayDTO> getCharacterInGame() throws ClientErrorException;

    /**
     * Возращает информацию о победе мафии или мирных,
     * если никто не побеждает, сообщеает об этом.
     * @param room - комната в которй подсчитывается колличество мафии и мирных
     * @return - возращет Enum о статусе игры.
     */
    public GameEndStatus isMafiaVictoryInRoom(RoomDAO room);
}