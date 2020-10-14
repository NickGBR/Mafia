package org.dreamteam.mafia.security;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.model.SecurityUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public final class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

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
        if (token.toString().equals("123")) {
            UserDAO dao = new UserDAO();
            dao.setLogin("admin");
            dao.setPassword("admin");
            return new SecurityUserDetails(dao);
        } else {
            throw new UsernameNotFoundException("Cannot find user with authentication token=" + token);
        }
      /*  return Optional
                .ofNullable(token)
                .map(String::valueOf)
                .flatMap(auth::findByToken)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Cannot find user with authentication token=" + token));*/
    }
}
