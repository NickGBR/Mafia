package org.dreamteam.mafia.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

/**
 * Подписанный JWT. Является токеном, но никого не аутентифицирует до верификации подписии
 */
@Getter
public class SignedJsonWebToken extends AbstractAuthenticationToken {
    private final String token;
    private final String login;

    public SignedJsonWebToken(String token) {
        super(Collections.EMPTY_LIST);
        this.token = token;
        this.login = null;
    }

    public SignedJsonWebToken(
            String login,
            String token,
            Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.setAuthenticated(true);
        this.token = token;
        this.login = login;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return login;
    }
}
