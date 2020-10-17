package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.VotingDAO;
import org.springframework.data.repository.CrudRepository;

public interface VotingRepository extends CrudRepository<VotingDAO, Long> {
}
