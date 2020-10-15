package org.dreamteam.mafia.security;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.model.SecurityUserDetails;
import org.dreamteam.mafia.model.SignedJsonWebToken;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.service.api.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    TokenService tokenService;
    UserRepository repository;

    @Autowired
    public TokenAuthenticationProvider(TokenService tokenService, UserRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @Override
    protected void additionalAuthenticationChecks(
            UserDetails userDetails,
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        // Намеренно оставлено пустым.
    }

    @Override
    protected UserDetails retrieveUser(
            String userName,
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        final Object token = usernamePasswordAuthenticationToken.getCredentials();
        System.out.println(userName + " : " + token);
        Optional<String> login = tokenService.extractUsernameFrom(new SignedJsonWebToken(token.toString()));
        if (login.isPresent()) {
            UserDAO dao = repository.getUserByLogin(login.get());
            return new SecurityUserDetails(dao);
        } else {
            throw new UsernameNotFoundException("Cannot find user with authentication token=" + token);
        }
    }
}
