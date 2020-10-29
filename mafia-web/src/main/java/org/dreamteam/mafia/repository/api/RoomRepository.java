package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.entities.RoomEntity;
import org.dreamteam.mafia.entities.UserEntity;
import org.dreamteam.mafia.model.GameStatusEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends CrudRepository<RoomEntity, Long> {

    /**
     * Поиск комнат согласно игровым состояниям.
     *
     * @param gameStatusEnum состояние игры в комнате: NOT_STARTED, IN_PROGRESS, COMPLETED, DELETED
     * @return список комнат
     */
    List<RoomEntity> findRoomDAOByGameStatus(GameStatusEnum gameStatusEnum);

    /**
     * Возвращает комнату, в которой находится указанный пользователь
     *
     * @param user - пользователь, для которого нужна найти комнату
     * @return - комнату пользователя или пустой Optional, если пользователь не находится в комнате
     */
    Optional<RoomEntity> findRoomDAOByUserListContains(UserEntity user);

}
