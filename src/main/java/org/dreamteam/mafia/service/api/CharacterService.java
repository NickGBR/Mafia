package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.dto.CharacterDTO;

public interface CharacterService {
    void saveOrUpdateCharacter(CharacterDTO characterDTO);
    void findCharacter(CharacterDTO characterDTO);
}
