package org.dreamteam.mafia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Описание комнаты, отправляемое клиенту
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private long roomId;
    private String name;
    private String description;
    private int currentPlayers;
    private int maxPlayers;
}
