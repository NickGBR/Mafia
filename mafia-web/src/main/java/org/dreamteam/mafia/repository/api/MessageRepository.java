package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.entities.MessageEntity;
import org.dreamteam.mafia.entities.RoomEntity;
import org.dreamteam.mafia.model.DestinationEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data репозиторий для обеспечения CRUD доступа к сообщениям
 */
public interface MessageRepository extends PagingAndSortingRepository<MessageEntity, Long> {

    /**
     * Поиск сообщений с указанным адресатами
     *
     * @param destinations - адресаты
     * @return - список сообщений по адресату
     */
    List<MessageEntity> findByDestinationIn(Collection<DestinationEnum> destinations, Pageable pageable);

    /**
     * Поиск сообщений комнаты с заданным адресатами
     *
     * @param roomEntity   - комната
     * @param destinations - адресаты
     * @return - список сообщений для адресата в комнате
     */
    List<MessageEntity> findByRoomAndDestinationIn(
            RoomEntity roomEntity, Collection<DestinationEnum> destinations, Pageable pageable);
}
