package org.dreamteam.mafia.security;

import org.dreamteam.mafia.entities.UserEntity;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.service.api.TokenService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class TokenAuthenticationProviderTest {

    @Mock
    UserRepository mockRepository;
    @Mock
    TokenService mockTokenService;
    @Mock
    SignedJsonWebToken mockToken;
    String token;
    UserEntity daoNormal;
    @InjectMocks
    private TokenAuthenticationProvider testedService;

    @Before
    public void setUp() {
        token = "42";
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockToken.getCredentials()).thenReturn(token);
        daoNormal = new UserEntity();
        daoNormal.setLogin("a");
        daoNormal.setPasswordHash("b");
    }

    @Test
    public void retrieveUserPositive() {
        Mockito.when(mockRepository.findByLogin(daoNormal.getLogin()))
                .thenReturn(Optional.of(daoNormal));
        Mockito.when(mockTokenService.extractUsernameFrom(Mockito.any(SignedJsonWebToken.class))).thenReturn(
                Optional.of(daoNormal.getLogin()));
        try {
            testedService.authenticate(mockToken);
        } catch (UsernameNotFoundException e) {
            Assert.fail("Failed to authenticate valid user");
        }
        Mockito.verify(mockRepository, Mockito.times(1)).findByLogin(Mockito.anyString());
        Mockito.verify(mockTokenService, Mockito.times(1)).extractUsernameFrom(Mockito.any(SignedJsonWebToken.class));
    }

    @Test
    public void retrieveUserInvalidToken() {
        Mockito.when(mockRepository.findByLogin(daoNormal.getLogin()))
                .thenReturn(Optional.of(daoNormal));
        Mockito.when(mockTokenService.extractUsernameFrom(Mockito.any(SignedJsonWebToken.class))).thenReturn(
                Optional.empty());
        try {
            testedService.authenticate(mockToken);
            Assert.fail("Successfully authenticated user with invalid token");
        } catch (UsernameNotFoundException ignored) {
        }
        Mockito.verify(mockRepository, Mockito.never()).findByLogin(Mockito.anyString());
        Mockito.verify(mockTokenService, Mockito.times(1)).extractUsernameFrom(Mockito.any(SignedJsonWebToken.class));
    }

    @Test
    public void retrieveUserNoSuchUser() {
        Mockito.when(mockRepository.findByLogin(daoNormal.getLogin()))
                .thenReturn(Optional.empty());
        Mockito.when(mockTokenService.extractUsernameFrom(Mockito.any(SignedJsonWebToken.class))).thenReturn(
                Optional.of(daoNormal.getLogin()));
        try {
            testedService.authenticate(mockToken);
            Assert.fail("Successfully authenticated user which is missing from the repository");
        } catch (UsernameNotFoundException ignored) {
        }
        Mockito.verify(mockRepository, Mockito.times(1)).findByLogin(Mockito.anyString());
        Mockito.verify(mockTokenService, Mockito.times(1)).extractUsernameFrom(Mockito.any(SignedJsonWebToken.class));
    }
}