package org.dreamteam.mafia;

import org.dreamteam.mafia.dao.MessageDAO;
import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;
import org.dreamteam.mafia.repository.api.CrudUserRepository;
import org.dreamteam.mafia.repository.api.MessageRepository;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Testing {

    @Bean
    public CommandLineRunner demo(CrudUserRepository userRepository,
                                  RoomRepository roomRepository) {
        return (args) -> {

            List<UserDAO> byRoomId = userRepository.findByRoomId(2);

            for (UserDAO u : byRoomId) {
                System.out.println(u);
            }



/*           UserDAO user = repository.findByLogin("user1").get(0);
            System.out.println(user);

            repository.save(new UserDAO("123456789", "Poly3"));
        };*/
        };
    }
}
