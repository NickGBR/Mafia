package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dto.RegistrationDTO;
import org.dreamteam.mafia.exceptions.UserRegistrationException;
import org.dreamteam.mafia.model.SecurityUserDetails;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class SpringSecurityBasedUserServiceTest {

    @InjectMocks
    private SpringSecurityBasedUserService testedService;

    @Mock
    UserRepository mockRepository;
    @Mock
    PasswordEncoder mockEncoder;

    RegistrationDTO dtoNormal, dtoPasswordMismatch;
    UserDAO daoNormal;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockEncoder.encode(Mockito.anyString())).thenAnswer(i -> i.getArguments()[0]);

        daoNormal = new UserDAO();
        daoNormal.setLogin("a");
        daoNormal.setPassword("b");

        dtoNormal = new RegistrationDTO();
        dtoNormal.setLogin("a");
        dtoNormal.setPassword("b");
        dtoNormal.setPasswordConfirmation("b");

        dtoPasswordMismatch = new RegistrationDTO();
        dtoPasswordMismatch.setLogin("c");
        dtoPasswordMismatch.setPassword("d");
        dtoPasswordMismatch.setPasswordConfirmation("e");
    }

    @Test
    public void registerNewUserPositive() throws Exception {
        try {
            testedService.registerNewUser(dtoNormal);
        } catch (UserRegistrationException e) {
            Assert.fail("Failed to register valid user");
        }
        Mockito.verify(mockRepository, Mockito.times(1)).saveUser(Mockito.any(UserDAO.class));
    }

    @Test
    public void registerNewUserPasswordMismatch() throws Exception {
        try {
            testedService.registerNewUser(dtoPasswordMismatch);
            Assert.fail("Registered user with mismatched passwords");
        } catch (UserRegistrationException ignored) {
        }
        Mockito.verify(mockRepository, Mockito.never()).saveUser(Mockito.any(UserDAO.class));
    }

    @Test
    public void registerNewUserDBRefused() throws Exception {
        Mockito.doThrow(new Exception()).when(mockRepository).saveUser(Mockito.any(UserDAO.class));
        try {
            testedService.registerNewUser(dtoPasswordMismatch);
            Assert.fail("Registered user when repository refused to perform saving");
        } catch (UserRegistrationException ignored) {
        }
        Mockito.verify(mockRepository, Mockito.never()).saveUser(Mockito.any(UserDAO.class));
    }

    @Test
    public void getCurrentUserPositive() {
        SecurityUserDetails securityUserDetails = new SecurityUserDetails(daoNormal);
        Authentication auth =
                new UsernamePasswordAuthenticationToken(securityUserDetails, null,
                                                        securityUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        Mockito.when(mockRepository.getUserByLogin(daoNormal.getLogin())).thenReturn(daoNormal);
        Optional<User> user = testedService.getCurrentUser();
        Assert.assertTrue("Failed to return properly authorised user", user.isPresent());
        Assert.assertEquals("Returned different user, than currently authorised", daoNormal.getLogin(),
                            user.get().getLogin());
        SecurityContextHolder.clearContext();
    }

    @Test
    public void getCurrentUserNoContext() {
        SecurityContextHolder.clearContext();
        Optional<User> user = testedService.getCurrentUser();
        Mockito.verify(mockRepository, Mockito.never()).getUserByLogin(Mockito.anyString());
        Assert.assertFalse("Found user, when there were no one authorised", user.isPresent());
        SecurityContextHolder.clearContext();
    }

    @Test
    public void getCurrentUserAnonymous() {
        SecurityUserDetails securityUserDetails = new SecurityUserDetails(daoNormal);
        Authentication auth =
                new AnonymousAuthenticationToken("non_empty", daoNormal, securityUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        Mockito.when(mockRepository.getUserByLogin("test")).thenReturn(daoNormal);
        Optional<User> user = testedService.getCurrentUser();
        Mockito.verify(mockRepository, Mockito.never()).getUserByLogin(Mockito.anyString());
        Assert.assertFalse("Found user, when authorised one was anonymous", user.isPresent());
        SecurityContextHolder.clearContext();
    }

    @Test
    public void getCurrentUserNotInRepository() {
        SecurityUserDetails securityUserDetails = new SecurityUserDetails(daoNormal);
        Authentication auth =
                new UsernamePasswordAuthenticationToken(securityUserDetails, null,
                                                        securityUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        Optional<User> user = testedService.getCurrentUser();
        Mockito.verify(mockRepository, Mockito.times(1)).getUserByLogin(daoNormal.getLogin());
        Assert.assertFalse("Found user, when the authorised one wasn't in repository", user.isPresent());
        SecurityContextHolder.clearContext();
    }
    
}