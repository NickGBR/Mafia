package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.GameDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface GameRepository extends CrudRepository<GameDAO, Long> {
}
