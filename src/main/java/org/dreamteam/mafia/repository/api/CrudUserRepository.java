package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.UserDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrudUserRepository extends CrudRepository<UserDAO, Long> {

   List<UserDAO> findByLogin(String login);

   //long nextId()

}