package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.Character;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CharacterRepository extends CrudRepository<Character, Long> {
}