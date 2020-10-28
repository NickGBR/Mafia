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
    private Integer maxUserAmount;
    private Boolean privateRoom;
    private Integer mafiaAmount;
    private Boolean hasSheriff;
    private Boolean hasDon;

    public RoomDisplayDTO(RoomDAO dao) {
        this.setName(dao.getName());
        this.setId(dao.getRoomId());
        this.setDescription(dao.getDescription());
        this.setMaxUserAmount(dao.getMaxUsersAmount());
        this.setPrivateRoom(!dao.getPasswordHash().equals(""));
        this.setCurrPlayers(dao.getUserList().size());
        this.setMafiaAmount(dao.getMafia());
        this.setHasSheriff(dao.getSheriff());
        this.setHasDon(dao.getDon());
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
