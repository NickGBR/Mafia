package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface GameRepository extends CrudRepository<Game, Long> {
}
