package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dao.enums.CharacterEnum;
import org.dreamteam.mafia.dao.enums.GamePhaseEnum;
import org.dreamteam.mafia.exceptions.IllegalGamePhaseException;
import org.dreamteam.mafia.util.ClientErrorCode;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl {

    public boolean isSheriff(UserDAO user, RoomDAO room) throws IllegalGamePhaseException {
        if (!room.getGamePhase().equals(GamePhaseEnum.NIGHT))
            throw new IllegalGamePhaseException(ClientErrorCode.WRONG_GAME_PHASE, "Wrong game phase");
        else return user.getCharacter().equals(CharacterEnum.SHERIFF);
    }


    public boolean isMafia(UserDAO user, RoomDAO room) throws IllegalGamePhaseException {
        if (!room.getGamePhase().equals(GamePhaseEnum.DAY))
          throw new IllegalGamePhaseException(ClientErrorCode.WRONG_GAME_PHASE, "Wrong game phase");
        else return user.getCharacter().equals(CharacterEnum.DON);
    }
}
