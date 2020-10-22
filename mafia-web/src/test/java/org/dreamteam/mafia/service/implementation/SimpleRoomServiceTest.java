package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;
import org.dreamteam.mafia.dto.RoomCreationDTO;
import org.dreamteam.mafia.dto.RoomDisplayDTO;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.dreamteam.mafia.service.api.UserService;
import org.dreamteam.mafia.util.ClientErrorCode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SimpleRoomServiceTest {

    @Mock
    RoomRepository mockRepository;
    @Mock
    PasswordEncoder mockEncoder;
    @Mock
    UserService mockUserService;
    @Mock
    Set<UserDAO> users;
    RoomCreationDTO publicDto, privateDto;
    @InjectMocks
    private SimpleRoomService testedService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockEncoder.encode(Mockito.anyString())).thenAnswer(i -> i.getArguments()[0]);

        publicDto = new RoomCreationDTO();
        publicDto.setName("public");
        publicDto.setDescription("test");
        publicDto.setPassword("");
        publicDto.setMaxPlayers(5);
        publicDto.setDon(false);
        publicDto.setSheriff(true);
        publicDto.setMafia(2);
        privateDto = new RoomCreationDTO();
        privateDto.setName("private");
        privateDto.setDescription("test-test");
        privateDto.setPassword("password");
        privateDto.setMaxPlayers(42);
        privateDto.setDon(true);
        privateDto.setSheriff(false);
        privateDto.setMafia(12);
    }

    @Test
    public void createRoomPublic() {
        UserDAO dao = new UserDAO();
        Mockito.when(mockUserService.getCurrentUserDAO()).thenReturn(Optional.of(dao));
        Mockito.when(mockRepository.findRoomDAOByUserListContains(dao)).thenReturn(Optional.empty());
        try {
            testedService.createRoom(publicDto);
        } catch (ClientErrorException e) {
            Assert.fail("Thrown an exception, when user is authorised and not in a room");
        }
        ArgumentCaptor<RoomDAO> captor = ArgumentCaptor.forClass(RoomDAO.class);
        Mockito.verify(mockRepository).save(captor.capture());
        RoomDAO roomDAO = captor.getValue();
        Assert.assertEquals("Created DAO has a wrong name", publicDto.getName(), roomDAO.getName());
        Assert.assertEquals("Created DAO has a wrong description", publicDto.getDescription(),
                            roomDAO.getDescription());
        Assert.assertEquals("Created DAO has a wrong password hash", publicDto.getPassword(),
                            roomDAO.getPasswordHash());
        Assert.assertEquals("Created DAO has a wrong mafia amount", publicDto.getMafia(), (long) roomDAO.getMafia());
        Assert.assertEquals("Created DAO has a wrong players amount", publicDto.getMaxPlayers(),
                            (long) roomDAO.getMaxUsersAmount());
        Assert.assertEquals("Created DAO has a wrong Don setting", publicDto.isDon(), roomDAO.getDon());
        Assert.assertEquals("Created DAO has a wrong Sheriff setting", publicDto.isSheriff(), roomDAO.getSheriff());
        Mockito.verify(mockEncoder, Mockito.never()).encode(Mockito.anyString());
    }

    @Test
    public void createRoomPrivate() {
        UserDAO dao = new UserDAO();
        Mockito.when(mockUserService.getCurrentUserDAO()).thenReturn(Optional.of(dao));
        Mockito.when(mockRepository.findRoomDAOByUserListContains(dao)).thenReturn(Optional.empty());
        try {
            testedService.createRoom(privateDto);
        } catch (ClientErrorException e) {
            Assert.fail("Thrown an exception, when user is authorised and not in a room");
        }
        ArgumentCaptor<RoomDAO> captor = ArgumentCaptor.forClass(RoomDAO.class);
        Mockito.verify(mockRepository).save(captor.capture());
        RoomDAO roomDAO = captor.getValue();
        Assert.assertEquals("Created DAO has a wrong name", privateDto.getName(), roomDAO.getName());
        Assert.assertEquals("Created DAO has a wrong description", privateDto.getDescription(),
                            roomDAO.getDescription());
        Assert.assertEquals("Created DAO has a wrong password hash", privateDto.getPassword(),
                            roomDAO.getPasswordHash());
        Assert.assertEquals("Created DAO has a wrong mafia amount", privateDto.getMafia(), (long) roomDAO.getMafia());
        Assert.assertEquals("Created DAO has a wrong players amount", privateDto.getMaxPlayers(),
                            (long) roomDAO.getMaxUsersAmount());
        Assert.assertEquals("Created DAO has a wrong Don setting", privateDto.isDon(), roomDAO.getDon());
        Assert.assertEquals("Created DAO has a wrong Sheriff setting", privateDto.isSheriff(), roomDAO.getSheriff());
        Mockito.verify(mockEncoder, Mockito.times(1)).encode(Mockito.anyString());
    }

    @Test
    public void createRoomNoUser() {
        Mockito.when(mockUserService.getCurrentUserDAO()).thenReturn(Optional.empty());
        try {
            testedService.createRoom(publicDto);
            Assert.fail("Successfully created room, when user is anonymous");
        } catch (ClientErrorException e) {
            Assert.fail("Thrown a wrong exception, when user is anonymous");
        } catch (SecurityException ignored) {
        }
    }

    @Test
    public void createRoomAlreadyInRoom() {
        UserDAO dao = new UserDAO();
        Mockito.when(mockUserService.getCurrentUserDAO()).thenReturn(Optional.of(dao));
        Mockito.when(mockRepository.findRoomDAOByUserListContains(dao)).thenReturn(Optional.of(new RoomDAO()));
        try {
            testedService.createRoom(publicDto);
            Assert.fail("Created a room, when current user is already in another room");
        } catch (ClientErrorException e) {
            Assert.assertEquals(ClientErrorCode.ALREADY_IN_ROOM, e.getCode());
        }
    }

    @Test
    public void getAvailableRooms() {
        RoomDAO daoPrivate = new RoomDAO();
        daoPrivate.setRoomId(42L);
        daoPrivate.setName("Test private room");
        daoPrivate.setDescription("Test private room description");
        daoPrivate.setPasswordHash("has hash");
        daoPrivate.setUserList(users);
        Mockito.when(users.size()).thenReturn(42);
        daoPrivate.setMaxUsersAmount(43);
        RoomDAO daoPublic = new RoomDAO();
        daoPublic.setRoomId(42L);
        daoPublic.setName("Test public room");
        daoPublic.setDescription("Test public room description");
        daoPublic.setPasswordHash("");
        daoPublic.setUserList(users);
        daoPublic.setMaxUsersAmount(77);
        Mockito.when(mockRepository.findRoomDAOByGameStatus(GameStatusEnum.NOT_STARTED)).thenReturn(
                Arrays.asList(daoPrivate, daoPublic));
        List<RoomDisplayDTO> rooms = testedService.getAvailableRooms();
        Mockito.verify(mockRepository, Mockito.times(1)).findRoomDAOByGameStatus(GameStatusEnum.NOT_STARTED);
        Assert.assertEquals("Returned wrong amount of rooms", 2, rooms.size());
        Assert.assertEquals("Returned incorrect room name", daoPrivate.getName(), rooms.get(0).getName());
        Assert.assertEquals("Returned incorrect description", daoPrivate.getDescription(),
                            rooms.get(0).getDescription());
        Assert.assertEquals("Returned incorrect id", daoPrivate.getRoomId(), rooms.get(0).getId());
        Assert.assertEquals("Returned incorrect current amount of users inside", users.size(),
                            (long) rooms.get(0).getCurrPlayers());
        Assert.assertEquals("Returned incorrect maximum possible amount of users", daoPrivate.getMaxUsersAmount(),
                            rooms.get(0).getMaxPlayers());
        Assert.assertTrue("Incorrectly marked room as public", rooms.get(0).getPrivateRoom());
        Assert.assertEquals("Returned incorrect room name", daoPublic.getName(), rooms.get(1).getName());
        Assert.assertEquals("Returned incorrect description", daoPublic.getDescription(),
                            rooms.get(1).getDescription());
        Assert.assertEquals("Returned incorrect id", daoPublic.getRoomId(), rooms.get(1).getId());
        Assert.assertEquals("Returned incorrect current amount of users inside", users.size(),
                            (long) rooms.get(1).getCurrPlayers());
        Assert.assertEquals("Returned incorrect maximum possible amount of users", daoPublic.getMaxUsersAmount(),
                            rooms.get(1).getMaxPlayers());
        Assert.assertFalse("Incorrectly marked room as private", rooms.get(1).getPrivateRoom());
    }
}