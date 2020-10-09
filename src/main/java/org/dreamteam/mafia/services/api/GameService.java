package org.dreamteam.mafia.services.api;

import org.dreamteam.mafia.model.*;
import org.dreamteam.mafia.model.Character;

import java.util.List;

public interface GameService {

    Role getRole(Game game, User user);

    List<Character> getCharactersInGame(Game game);

    List<Message> getMessageLog(Game game);

    List<Character> getAliveCharactersInGame(Game game);

    Character getNextSpeaker(Game game);



}
