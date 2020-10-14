package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dto.LoginDTO;
import org.dreamteam.mafia.dto.RegistrationDTO;
import org.dreamteam.mafia.exceptions.UserAuthenticationException;
import org.dreamteam.mafia.exceptions.UserRegistrationException;
import org.dreamteam.mafia.model.SignedJsonWebToken;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.service.api.TokenService;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Реализация интерфейса сервиса данных пользователей, работающая со Spring Security и репозиторием
 */
@Service("securityUserService")
public class SpringSecurityBasedUserService implements UserService {


    UserRepository repository;
    PasswordEncoder encoder;
    TokenService tokenService;

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
            throw new UserRegistrationException(ResultCode.PASSWORD_MISMATCH, "Password mismatch!");
        }
        UserDAO user = new UserDAO();
        user.setLogin(registrationDTO.getLogin());
        user.setPassword(encoder.encode(registrationDTO.getPassword()));
        try {
            repository.saveUser(user);
        } catch (Exception e) {
            throw new UserRegistrationException(ResultCode.USER_ALREADY_EXISTS, "Login is already in the database");
        }
    }

    @Override
    public SignedJsonWebToken loginUser(LoginDTO loginDTO) throws UserAuthenticationException {
        UserDAO userDAO = repository.getUserByLogin(loginDTO.getLogin());
        if (userDAO != null) {
            if (!encoder.matches(loginDTO.getPassword(), userDAO.getPassword())) {
                throw new UserAuthenticationException(ResultCode.INCORRECT_PASSWORD,
                                                      "Supplied password do not match login");
            }

            return tokenService.getTokenFor(new User(userDAO));
        } else {
            throw new UserAuthenticationException(ResultCode.USER_NOT_EXISTS,
                                                  "User '" + loginDTO.getLogin() + "' not found in repository");
        }
    }

    /*TODO Разобрать ситуацию, когда пользователь аутентифицирован,
       но отстутствует в репозитории: ака репозиторий потерял пользователя между вызовами
       loadUserByUsername из Spring Security и getCurrentUser сейчас.
     */
    @Override
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            UserDAO user = repository.getUserByLogin(currentUserName);
            if (user != null) {
                return Optional.of(new User(user));
            }
        }
        return Optional.empty();
    }

}
