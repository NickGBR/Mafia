package org.dreamteam.mafia.service.implementation;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import org.dreamteam.mafia.model.SignedJsonWebToken;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.service.api.TokenService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static io.jsonwebtoken.impl.TextCodec.BASE64;

@Service
public class TimeLimitedTokenService implements TokenService {

    private final static long TIME_LIMIT_IN_HOURS = 24;
    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    @Override
    public SignedJsonWebToken getTokenFor(User user) {
        final Instant now = Instant.now();
        final Instant expiredBy = now.plus(Duration.ofHours(TIME_LIMIT_IN_HOURS));
        final Claims claims = Jwts
                .claims()
                .setExpiration(Date.from(expiredBy))
                .setIssuedAt(Date.from(now))
                .setSubject(user.getLogin());
        return new SignedJsonWebToken(
                Jwts.builder()
                        .setClaims(claims)
                        .signWith(SignatureAlgorithm.HS256, BASE64.encode("abc"))
                        .compressWith(COMPRESSION_CODEC)
                        .compact()
        );
    }

    @Override
    public Optional<String> extractUsernameFrom(SignedJsonWebToken token) {
        final JwtParser parser = Jwts
                .parser()
                .setSigningKey(BASE64.encode("abc"));
        try {
            Claims claims = parser.parseClaimsJws(token.getValue()).getBody();
            return Optional.ofNullable(claims.getSubject());
        } catch (JwtException e) {
            return Optional.empty();
        }
    }
}
