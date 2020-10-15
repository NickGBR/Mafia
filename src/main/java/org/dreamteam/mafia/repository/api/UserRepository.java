package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.model.User;

public interface UserRepository {

    void saveUser(UserDAO user) throws Exception;
    UserDAO getUserByLogin(String login);

}