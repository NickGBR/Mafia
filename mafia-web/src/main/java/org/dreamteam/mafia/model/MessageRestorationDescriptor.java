package org.dreamteam.mafia.model;

import lombok.Getter;
import lombok.Setter;
import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.enums.DestinationEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс для описания того, какие сообщения пользователь может получить из архива при запросе
 * восстановления состояния страницы.
 */
@Getter
@Setter
public class MessageRestorationDescriptor {

    private final List<DestinationEnum> destinations;
    private RoomDAO room = null;

    public MessageRestorationDescriptor() {
        destinations = new ArrayList<>();
    }

    public Optional<RoomDAO> getRoom() {
        return Optional.ofNullable(room);
    }
}