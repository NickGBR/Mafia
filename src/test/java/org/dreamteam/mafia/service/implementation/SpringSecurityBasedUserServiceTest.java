package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dto.UserDTO;
import org.dreamteam.mafia.exceptions.UserRegistrationException;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

public class SpringSecurityBasedUserServiceTest {
    
    @InjectMocks
    private SpringSecurityBasedUserService testedService;

    @Mock
    UserRepository mockRepository;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void registerNewUser() {
        UserDTO dto = new UserDTO();
        try {
            testedService.registerNewUser(dto);
        } catch (UserRegistrationException e) {
            Assert.fail();
        }
        Mockito.verify(mockRepository, Mockito.times(1)).saveUser(Mockito.any(User.class));
    }

    @Test
    public void getCurrentUser() {
    }
}