package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dto.CharacterDTO;
import org.dreamteam.mafia.exceptions.GameIsOverException;
import org.dreamteam.mafia.exceptions.GameNotStartedException;
import org.dreamteam.mafia.exceptions.IllegalMoveException;
import org.dreamteam.mafia.model.Character;
import org.dreamteam.mafia.model.*;
import org.dreamteam.mafia.repository.api.GameRepository;
import org.dreamteam.mafia.service.api.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public Game getGameInRoom(Room room) throws GameNotStartedException {
        return null;
    }

    @Override
    public List<Character> getCharactersInGame(Game game) {
        return null;
    }

    @Override
    public List<Message> getMessageLog(Game game) {
        return null;
    }

    @Override
    public void advancePhase(Game game) throws GameIsOverException {

    }

    @Override
    public void nominateCharacter(User user, CharacterDTO characterDTO) throws IllegalMoveException {

    }

    @Override
    public void voteCharacter(User user, CharacterDTO characterDTO) throws IllegalMoveException {

    }
}
