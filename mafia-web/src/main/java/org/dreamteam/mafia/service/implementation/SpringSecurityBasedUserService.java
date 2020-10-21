package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dto.LoginDTO;
import org.dreamteam.mafia.dto.RegistrationDTO;
import org.dreamteam.mafia.exceptions.UserAuthenticationException;
import org.dreamteam.mafia.exceptions.UserRegistrationException;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.security.SignedJsonWebToken;
import org.dreamteam.mafia.service.api.TokenService;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.util.ClientErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Реализация интерфейса сервиса данных пользователей, работающая со Spring Security
 */
@Service("securityUserService")
public class SpringSecurityBasedUserService implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;

    @Autowired
    public SpringSecurityBasedUserService(
            UserRepository repository,
            PasswordEncoder encoder,
            TokenService tokenService) {
        this.repository = repository;
        this.encoder = encoder;
        this.tokenService = tokenService;
    }

    @Override
    public void registerNewUser(RegistrationDTO registrationDTO) throws UserRegistrationException {
        if (!registrationDTO.getPassword().equals(registrationDTO.getPasswordConfirmation())) {
            throw new UserRegistrationException(ClientErrorCode.PASSWORD_MISMATCH, "Password mismatch!");
        }
        Optional<UserDAO> sameLoginDao = repository.findByLogin(registrationDTO.getLogin());
        if (sameLoginDao.isPresent()) {
            throw new UserRegistrationException(ClientErrorCode.USER_ALREADY_EXISTS,
                                                "Login is already in the database");
        }
        UserDAO user = new UserDAO();
        user.setLogin(registrationDTO.getLogin());
        user.setPasswordHash(encoder.encode(registrationDTO.getPassword()));

        repository.save(user);
    }

    @Override
    public SignedJsonWebToken loginUser(LoginDTO loginDTO) throws UserAuthenticationException {
        Optional<UserDAO> userDAO = repository.findByLogin(loginDTO.getLogin());
        if (userDAO.isPresent()) {
            if (!encoder.matches(loginDTO.getPassword(), userDAO.get().getPasswordHash())) {
                throw new UserAuthenticationException(ClientErrorCode.INCORRECT_PASSWORD,
                                                      "Supplied password do not match login");
            }

            return tokenService.getTokenFor(new User(userDAO.get()));
        } else {
            throw new UserAuthenticationException(ClientErrorCode.USER_NOT_EXISTS,
                                                  "User '" + loginDTO.getLogin() + "' not found in repository");
        }
    }

    @Override
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            Optional<UserDAO> userDAO = repository.findByLogin(currentUserName);
            if (userDAO.isPresent()) {
                return Optional.of(new User(userDAO.get()));
            } else {
                throw new RuntimeException(
                        "User is authenticated, but is not present in repository. Internal logic failure");
            }
        }
        return Optional.empty();
    }
}
