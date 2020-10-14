package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dto.RegistrationDTO;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.security.RepositoryBasedUserDetailsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class RepositoryBasedUserDetailsServiceTest {

    @InjectMocks
    private RepositoryBasedUserDetailsService testedService;

    @Mock
    UserRepository mockRepository;

    RegistrationDTO dtoNormal;
    UserDAO daoNormal;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        daoNormal = new UserDAO();
        daoNormal.setLogin("a");
        daoNormal.setPassword("b");

        dtoNormal = new RegistrationDTO();
        dtoNormal.setLogin("a");
        dtoNormal.setPassword("b");
        dtoNormal.setPasswordConfirmation("b");
    }


    @Test
    public void loadUserByUsernamePositive() {

        Mockito.when(mockRepository.getUserByLogin(daoNormal.getLogin())).thenReturn(daoNormal);
        try {
            UserDetails userDetails = testedService.loadUserByUsername(daoNormal.getLogin());
            Mockito.verify(mockRepository, Mockito.times(1)).getUserByLogin(daoNormal.getLogin());
            Assert.assertEquals("Returned wrong user details", daoNormal.getLogin(), userDetails.getUsername());
        } catch (UsernameNotFoundException e) {
            Assert.fail("Failed to load existing user details");
        }
    }

    @Test
    public void loadUserByUsernameNegative() {
        try {
            testedService.loadUserByUsername(daoNormal.getLogin());
            Assert.fail("Failed to load existing user details");
        } catch (UsernameNotFoundException ignored) {
        }
        Mockito.verify(mockRepository, Mockito.times(1)).getUserByLogin(daoNormal.getLogin());
    }
}