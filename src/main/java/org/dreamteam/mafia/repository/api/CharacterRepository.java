package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.CharacterDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CharacterRepository extends CrudRepository<CharacterDAO, Long> {

}
