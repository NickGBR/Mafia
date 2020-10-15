package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrudUserRepository extends CrudRepository<User, Long> {

   List<User> findByLogin(String login);

}