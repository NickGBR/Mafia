package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends CrudRepository<RoomDAO, Long> {

    /**
     * Поиск комнат согласно игровым состояниям.
     *
     * @param gameStatusEnum состояние игры в комнате: NOT_STARTED, IN_PROGRESS, COMPLETED, DELETED
     * @return список комнат
     */
    List<RoomDAO> findRoomDAOByGameStatus(GameStatusEnum gameStatusEnum);

    /**
     * Возвращает комнату, в которой находится указанный пользователь
     *
     * @param user - пользователь, для которого нужна найти комнату
     * @return - комнату пользователя или пустой Optional, если пользователь не находится в комнате
     */
    Optional<RoomDAO> findRoomDAOByUserListContains(UserDAO user);
}
