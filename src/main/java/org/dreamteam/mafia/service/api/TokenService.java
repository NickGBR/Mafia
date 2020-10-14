package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.model.SignedJsonWebToken;
import org.dreamteam.mafia.model.User;

import java.util.Optional;

public interface TokenService {

    SignedJsonWebToken getTokenFor(User user);

    Optional<String> extractUsernameFrom(SignedJsonWebToken token);
}
