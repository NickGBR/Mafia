package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends CrudRepository<User, Long> {


}
