package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.MessageDAO;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<MessageDAO, Long> {
}
