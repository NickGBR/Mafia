package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.User;
import org.dreamteam.mafia.dto.UserDTO;
import org.dreamteam.mafia.exceptions.UserRegistrationException;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerNewUser(UserDTO userDTO) throws UserRegistrationException {
/*        if (userDTO != null && userRepository.
            userRepository.save(userDTO);*/
        return null;
    }

    @Override
    public Optional<User> getCurrentUser(User currentUser) {
        return userRepository.findById(currentUser.getUserId());
    }

    @Override
    public Optional<User> findByLogin(UserDTO userDTO) {
        return null;
    }

}
