package org.dreamteam.mafia.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.enums.GamePhaseEnum;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;

/**
 * Игра - набор данных, описывающих конкретную игру в целом
 */
@Setter
@Getter
@NoArgsConstructor
public class Room {
    private boolean isStarted = false;
    private boolean isExisting = false;
    /* Используется для прерывания игры, если пришел объект Room c isInterrupted = true,
     то игра останавливается.*/
    private boolean isInterrupted = false;
    private boolean isNight = false;
    private String name;
    private String id;
    private Message message;
    private User admin;

    public Room(RoomDAO roomDAO) {
        name = roomDAO.getName();
        id = roomDAO.getRoomId().toString();
        isStarted = !roomDAO.getGameStatus().equals(GameStatusEnum.NOT_STARTED);
        isExisting = !roomDAO.getGameStatus().equals(GameStatusEnum.DELETED);
        isNight = roomDAO.getGamePhase().equals(GamePhaseEnum.CIVILIANS_PHASE);
    }
}
