package org.dreamteam.mafia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Описание комнаты для ее создания, принимаемое от клиента
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreationDTO {
    private String name = "";
    private String description = "";
    private String password = "";
    private int maxPlayers = 5;
    private int mafia = 1;
    private boolean don = false;
    private boolean sheriff = true;

    @Override
    public String toString() {
        return "RoomCreationDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", password='" + password + '\'' +
                ", maxPlayers=" + maxPlayers +
                ", mafia=" + mafia +
                ", don=" + don +
                ", sheriff=" + sheriff +
                '}';
    }
}
