package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.RoomDAO;

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

    public RoomDisplayDTO(RoomDAO dao) {
        this.setName(dao.getName());
        this.setId(dao.getRoomId());
        this.setDescription(dao.getDescription());
        this.setMaxPlayers(dao.getMaxUsersAmount());
        this.setPrivateRoom(!dao.getPasswordHash().equals(""));
        this.setCurrPlayers(dao.getUserList().size());
    }

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
