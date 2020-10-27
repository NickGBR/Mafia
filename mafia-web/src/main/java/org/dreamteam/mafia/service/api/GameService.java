package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.dto.CharacterDTO;
import org.dreamteam.mafia.exceptions.*;
import org.dreamteam.mafia.model.Character;
import org.dreamteam.mafia.model.Message;
import org.dreamteam.mafia.model.Room;
import org.dreamteam.mafia.model.User;

import java.util.List;

/**
 * Интерфейс сервиса, обеспечивающего ведение самой игры.
 */
public interface GameService {

    /**
     * Запускает игру.
     */
    void startGame() throws ClientErrorException;


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
     * @throws ClientErrorException - если игра уже окончена
     */
    void advancePhase(Room room) throws ClientErrorException;

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

}