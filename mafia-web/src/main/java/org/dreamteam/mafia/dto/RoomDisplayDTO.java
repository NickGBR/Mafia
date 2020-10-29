package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.model.Room;

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
    private Integer maxUserAmount;
    private Boolean privateRoom;
    private Integer mafiaAmount;
    private Boolean hasSheriff;
    private Boolean hasDon;

    public RoomDisplayDTO(Room room) {
        this.setName(room.getName());
        this.setId(room.getId());
        this.setDescription(room.getDescription());
        this.setMaxUserAmount(room.getMaxUserAmount());
        this.setPrivateRoom(room.getPrivateRoom());
        this.setCurrPlayers(room.getCurrPlayers());
        this.setMafiaAmount(room.getMafiaAmount());
        this.setHasSheriff(room.getHasSheriff());
        this.setHasDon(room.getHasDon());
    }

    @Override
    public String toString() {
        return "RoomDisplayDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", currPlayers=" + currPlayers +
                ", maxUserAmount=" + maxUserAmount +
                ", privateRoom=" + privateRoom +
                '}';
    }
}
