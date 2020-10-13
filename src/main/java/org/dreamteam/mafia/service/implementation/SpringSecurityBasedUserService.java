package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dto.UserDTO;
import org.dreamteam.mafia.exceptions.UserRegistrationException;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SpringSecurityBasedUserService implements UserService {

    UserRepository repository;

    @Autowired
    public SpringSecurityBasedUserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User registerNewUser(UserDTO userDTO) throws UserRegistrationException {
        return null;
    }

    @Override
    public Optional<User> getCurrentUser() {
        return Optional.empty();
    }
}
