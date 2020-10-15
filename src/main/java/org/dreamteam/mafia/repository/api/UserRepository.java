package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository {

    void saveUser(UserDAO user) throws Exception;
    UserDAO getUserByLogin(String login);

}