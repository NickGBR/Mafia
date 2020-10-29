package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.entities.RoomEntity;
import org.dreamteam.mafia.model.GamePhaseEnum;

/**
 * Описание обновления текущего игрового состояния для отправки клиенту
 */
@NoArgsConstructor
@Getter
@Setter
public class GameDTO {
    private GamePhaseEnum gamePhase;
    private String message;
    private Integer timer;

    public GameDTO(RoomEntity roomEntity) {
        gamePhase = roomEntity.getGamePhase();
    }
}
