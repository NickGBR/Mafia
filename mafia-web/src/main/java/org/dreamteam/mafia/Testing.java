package org.dreamteam.mafia;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;
import org.dreamteam.mafia.repository.api.CrudUserRepository;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Testing {


    public void test(CrudUserRepository userRepository,
                     RoomRepository roomRepository) {
        UserDAO existingUser = userRepository.findByLogin("user5").get(0);

    //   UserDAO newUser = userRepository.save(new UserDAO("22222", "new Password"));

        RoomDAO roomDAO = new RoomDAO("123", existingUser,
                "11111", 5,
                GameStatusEnum.NOT_STARTED);
        //roomDAO.setRoomId(100);
        roomRepository.save(roomDAO);


       /* UserDAO existingUser = userRepository.findByLogin("22222").get(0);
        Optional<RoomDAO> room = roomRepository.findById(5L);
        existingUser.setRoom(room.get());*/

    }

    @Bean
    public CommandLineRunner demo(CrudUserRepository userRepository,
                                  RoomRepository roomRepository) {
        return (args) -> {
            test(userRepository, roomRepository);
        };
    }
}

