package org.dreamteam.mafia.repository.implementation;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryDummy implements UserRepository {
    @Override
    public void saveUser(UserDAO user) {
        throw new UnsupportedOperationException("User repository is not mocked!");
    }

    @Override
    public UserDAO getUserByLogin(String login) {
        throw new UnsupportedOperationException("User repository is not mocked!");
    }
}
