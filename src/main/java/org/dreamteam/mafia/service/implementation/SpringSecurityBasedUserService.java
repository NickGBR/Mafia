package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dto.UserDTO;
import org.dreamteam.mafia.exceptions.UserRegistrationException;
import org.dreamteam.mafia.model.SecurityUserDetails;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис реализующий, сервис работы с пользователями для приложения, так и сервис работы с пользователям
 * для Spring Security. Отвечает за преобразование загруженной из репозитория сущности пользователя
 * в сущность ожидаему либо приложением, либо Spring Security.
 */
@Service("securityUserService")
public class SpringSecurityBasedUserService implements UserService, UserDetailsService {


    UserRepository repository;
    PasswordEncoder encoder;

    @Autowired
    public SpringSecurityBasedUserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public void registerNewUser(UserDTO userDTO) throws UserRegistrationException {
        if (!userDTO.getPassword().equals(userDTO.getPasswordConfirmation())) {
            throw  new UserRegistrationException(ResultCode.PASSWORD_MISMATCH, "Password mismatch!");
        }
        UserDAO user = new UserDAO();
        user.setLogin(userDTO.getLogin());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        try {
            repository.saveUser(user);
        } catch (Exception e) {
            throw new UserRegistrationException(ResultCode.USER_ALREADY_EXISTS, "Login is already in the database");
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

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDAO user = repository.getUserByLogin(userName);
        if (user != null) {
            return new SecurityUserDetails(user);
        } else {
            throw  new UsernameNotFoundException("User '" + userName + "' not found in repository");
        }
    }
}
