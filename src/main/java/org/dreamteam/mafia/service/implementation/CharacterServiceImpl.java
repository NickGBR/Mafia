package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dto.CharacterDTO;
import org.dreamteam.mafia.repository.api.CharacterRepository;
import org.dreamteam.mafia.service.api.CharacterService;
import org.springframework.stereotype.Service;

@Service
public class CharacterServiceImpl implements CharacterService {

    private CharacterRepository characterRepository;

    @Override
    public void saveOrUpdateCharacter(CharacterDTO characterDTO) {

    }

    @Override
    public void findCharacter(CharacterDTO characterDTO) {

    }
}
