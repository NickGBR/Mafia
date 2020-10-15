package org.dreamteam.mafia.security;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.model.SecurityUserDetails;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Сервис для обслуживания Spring Security. Извлекает из репозитория данные пользователя,
 * необходимые для аутентификации
 */
@Service("repositoryUserDetailsService")
public class RepositoryBasedUserDetailsService
        implements UserDetailsService {

    private final UserRepository repository;

    @Autowired
    public RepositoryBasedUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDAO user = repository.getUserByLogin(userName);
        if (user != null) {
            return new SecurityUserDetails(user);
        } else {
            throw new UsernameNotFoundException("User '" + userName + "' not found in repository");
        }
    }
}
