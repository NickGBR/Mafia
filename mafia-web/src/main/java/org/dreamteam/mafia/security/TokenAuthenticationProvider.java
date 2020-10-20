package org.dreamteam.mafia.security;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.service.api.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

/**
 * Класс обеспечивающий аутентификацию пользователя по токену.
 * Обращается к сервису токенов для получения из токена имени пользователя,
 * а затем пытается найти такового в базе.
 */
@Component
public final class TokenAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(TokenAuthenticationProvider.class);
    private final TokenService tokenService;
    private final UserRepository repository;

    @Autowired
    public TokenAuthenticationProvider(TokenService tokenService, UserRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final Object token = authentication.getCredentials();
        logger.debug("Retrieving user via token : " + token);
        Optional<String> login = tokenService.extractUsernameFrom((SignedJsonWebToken) authentication);
        if (login.isPresent()) {
            Optional<UserDAO> userDAO = repository.findByLogin(login.get());
            if (userDAO.isPresent()) {
                return new SignedJsonWebToken(login.get(), (String) token, Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_USER")));
            }
        }
        throw new UsernameNotFoundException("Cannot find user with authentication token: " + token);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(SignedJsonWebToken.class);
    }
}
