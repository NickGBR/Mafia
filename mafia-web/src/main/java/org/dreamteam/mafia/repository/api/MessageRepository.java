package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.MessageDAO;
import org.dreamteam.mafia.dao.RoomDAO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<MessageDAO, Long> {

    /**
     * Поиск сообщений комнаты.
     * @param roomDAO комната
     * @return список сообщений комнаты
     */
    List<MessageDAO> findByRoom(RoomDAO roomDAO);

}
