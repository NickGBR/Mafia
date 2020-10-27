package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dao.enums.CharacterEnum;
import org.dreamteam.mafia.dao.enums.CharacterStatusEnum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data репозиторий для обеспечения CRUD доступа к пользователям
 */
@Repository
public interface UserRepository extends CrudRepository<UserDAO, Long> {

    /**
     * Находит всех пользователей с указанным логином
     *
     * @param login - логин
     * @return - список пользователей. Для текущей БД всегла 0 или 1 пользователь, т.к. логин уникален
     */
    Optional<UserDAO> findByLogin(String login);

    /**
     * Поиск списка игроков комнаты по её номеру (идентификатору).
     *
     * @param roomId идентификатор комнаты
     * @return список игроков
     */
    @Query("select u from UserDAO u where u.room.roomId = ?1")
    List<UserDAO> findByRoomId(Integer roomId);

    /**
     * Текущее количество игроков в комнате.
     *
     * @param roomId идентификатор комнаты
     * @return текущее количество игроков
     */
    @Query("select count(u) from UserDAO u where u.room.roomId = ?1")
    Integer findCurrentUsersAmountByRoomId(Integer roomId);

    /**
     * Количество игроков в комнате готовых к игре.
     *
     * @param roomId идентификатор комнаты
     * @return количество игроков готовых к игре
     */
    @Query("select count(u) from UserDAO u where u.room.roomId = ?1 and u.isReady = true")
    Integer findUsersAmountByRoomIdReadyToPlay(Integer roomId);

    Long countByCharacterInAndAndCharacterStatusAndRoom(List<CharacterEnum> roles,
                                                        CharacterStatusEnum status, RoomDAO room);
}