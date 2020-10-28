package org.dreamteam.mafia.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.enums.DestinationEnum;

import java.util.Optional;

/**
 * Класс для описания назначения сообщения: содержит обозначение адресата и, возможно, целевую комнату,
 * если тип адресата того требует
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageDestinationDescriptor {

    private DestinationEnum destination;
    private RoomDAO room = null;

    public Optional<RoomDAO> getRoom() {
        return Optional.ofNullable(room);
    }
}
