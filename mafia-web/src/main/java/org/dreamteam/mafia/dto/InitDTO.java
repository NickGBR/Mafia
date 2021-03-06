package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.model.CharacterEnum;
import org.dreamteam.mafia.model.GamePhaseEnum;

import java.io.Serializable;

/**
 * Описание текущего контекста игрока для его восстановления, отправляемое клиенту
 * после перезагрузки последнего
 */
@Getter
@Setter
@NoArgsConstructor
public class InitDTO implements Serializable {

    Boolean isLoggedIn = false;
    Boolean isInRoom = false;
    String name = "";
    String roomID = "";
    String roomName = "";
    Boolean isAdmin = false;
    Boolean isReady = false;
    Integer maxUserAmount = 3;
    Integer mafiaAmount = 1;
    Boolean hasDon = false;
    Boolean hasSheriff = false;
    CharacterEnum role = null;
    Boolean isGameStarted = false;
    Boolean isAlive = true;

    GamePhaseEnum  gamePhase = GamePhaseEnum.CIVILIANS_DISCUSS_PHASE;
    Boolean isRoomReady = false;
}

