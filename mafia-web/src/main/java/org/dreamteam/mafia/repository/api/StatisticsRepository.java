package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.StatisticsDAO;
import org.springframework.data.repository.CrudRepository;

public interface StatisticsRepository extends CrudRepository<StatisticsDAO, Long> {
}
