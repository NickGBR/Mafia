package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dto.UserDTO;
import org.dreamteam.mafia.exceptions.UserRegistrationException;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.service.api.UserService;

import java.util.Optional;

public class SpringSecurityBasedUserService implements UserService {
    @Override
    public User registerNewUser(UserDTO userDTO) throws UserRegistrationException {
        return null;
    }

    @Override
    public Optional<User> getCurrentUser() {
        return Optional.empty();
    }
}
