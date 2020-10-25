package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.enums.GamePhaseEnum;

@NoArgsConstructor
@Getter
@Setter
public class GameDTO {
    private GamePhaseEnum gamePhase;
    private String message;

    public GameDTO(RoomDAO roomDAO){
        gamePhase = roomDAO.getGamePhase();
    }
}
