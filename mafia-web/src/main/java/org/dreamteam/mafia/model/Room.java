package org.dreamteam.mafia.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.entities.RoomEntity;

/**
 * Описание комнаты, передаваемое между сервисами и контроллерами.
 */
@Getter
@Setter
@NoArgsConstructor
public class Room {
    private Long id;
    private String name;
    private String description;
    private Integer currPlayers;
    private Integer maxUserAmount;
    private Boolean privateRoom;
    private Integer mafiaAmount;
    private Boolean hasSheriff;
    private Boolean hasDon;
    private GameStatusEnum status;
    private GamePhaseEnum phase;

    public Room(RoomEntity dao) {
        this.setName(dao.getName());
        this.setId(dao.getRoomId());
        this.setDescription(dao.getDescription());
        this.setMaxUserAmount(dao.getMaxUsersAmount());
        this.setPrivateRoom(!dao.getPasswordHash().equals(""));
        this.setCurrPlayers(dao.getUserList().size());
        this.setMafiaAmount(dao.getMafia());
        this.setHasSheriff(dao.getSheriff());
        this.setHasDon(dao.getDon());
        this.setStatus(dao.getGameStatus());
        this.setPhase(dao.getGamePhase());
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
