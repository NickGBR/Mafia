package org.dreamteam.mafia.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.entities.RoomEntity;

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
    private RoomEntity room = null;

    public Optional<RoomEntity> getRoom() {
        return Optional.ofNullable(room);
    }
}
