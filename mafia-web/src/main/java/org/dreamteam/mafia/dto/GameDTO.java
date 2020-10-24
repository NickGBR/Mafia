package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.Setter;
import org.dreamteam.mafia.dao.enums.GamePhaseEnum;

@Getter
@Setter
public class GameDTO {
    private GamePhaseEnum gamePhase;
}
