package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.MessageDAO;
import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.enums.DestinationEnum;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;

public interface MessageRepository extends PagingAndSortingRepository<MessageDAO, Long> {

    /**
     * Поиск сообщений комнаты.
     *
     * @param roomDAO комната
     * @return список сообщений комнаты
     */
    List<MessageDAO> findByRoom(RoomDAO roomDAO);

    /**
     * Поиск сообщений с указанным адресатами
     *
     * @param destinations - адресаты
     * @return - список сообщений по адресату
     */
    List<MessageDAO> findByDestinationIn(Collection<DestinationEnum> destinations);

    /**
     * Поиск сообщений комнаты с заданным адресатами
     *
     * @param roomDAO      - комната
     * @param destinations - адресаты
     * @return - список сообщений для адресата в комнате
     */
    List<MessageDAO> findByRoomAndDestinationIn(RoomDAO roomDAO, Collection<DestinationEnum> destinations);
}
