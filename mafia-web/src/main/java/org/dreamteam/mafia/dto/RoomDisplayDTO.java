package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Описание комнаты, отправляемое в клиент для ее отображения
 */
@Getter
@Setter
@NoArgsConstructor
public class RoomDisplayDTO {
    private Long id;
    private String name;
    private String description;
    private Integer currPlayers;
    private Integer maxPlayers;
    private Boolean privateRoom;

    @Override
    public String toString() {
        return "RoomDisplayDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", currPlayers=" + currPlayers +
                ", maxPlayers=" + maxPlayers +
                ", privateRoom=" + privateRoom +
                '}';
    }
}
