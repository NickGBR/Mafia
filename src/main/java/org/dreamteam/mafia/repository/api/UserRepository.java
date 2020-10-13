package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.model.User;

public interface UserRepository {

    void saveUser(User user);
    User getUserByID(Long userId);

}